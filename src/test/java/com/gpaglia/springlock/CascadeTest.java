package com.gpaglia.springlock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.transaction.TestTransaction;

import com.gpaglia.springlock.entities.Child;
import com.gpaglia.springlock.entities.Parent;

@SpringBootTest()
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class CascadeTest {
  @SuppressWarnings("unused")
  private static final Logger LOGGER = LoggerFactory.getLogger(SpringLockTest.class);

  /*
  @Autowired
  private ParentRepository parentRepo;
  
  @Autowired
  private ChildRepository childRepo;
  
  @Autowired
  private GrandChildRepository grandChildRepo;
  */
  
  @PersistenceContext
  private EntityManager em;
  
  @Test
  @Transactional
  public void testCascadeOnChildUpdate() {
    // set the ids
    final Long idParent = 275L;
    final Long idChild1 = 1001L;
    final Long idChild2 = 1002L;   
    
    // flag the current tx for commit
    TestTransaction.flagForCommit();

    Parent p = new Parent(idParent, "Parent_Name");
    
    Child c1 = new Child(idChild1, "Child1_Name");
    c1.setParent(p);
    
    Child c2 = new Child(idChild2, "Child2_Name");
    c2.setParent(p);
    
    em.persist(p);
    em.flush();
    
    TestTransaction.end();
    
    em.clear();
    
    // start a new transaction
    TestTransaction.start();
    TestTransaction.flagForCommit();
    
    Parent pnew = em.find(Parent.class, idParent);
    
    assertThat(pnew, notNullValue());
    
    List<Child> children = p.getChildren();
    
    assertThat(children, hasSize(2));
    Map<Long, Child> map = children.stream().collect(
        Collectors.toMap(c -> c.getId(), Function.identity()));
    
    assertThat(map.get(idChild1), notNullValue());
    assertThat(map.get(idChild1).getName(), is("Child1_Name"));
    
    assertThat(map.get(idChild2), notNullValue());
    assertThat(map.get(idChild2).getName(), is("Child2_Name"));

    TestTransaction.end();
    
    em.clear();
    
    // start a new transaction
    TestTransaction.start();
    TestTransaction.flagForCommit();

    Parent pnew2 = em.find(Parent.class, idParent);
    
    assertThat(pnew2, notNullValue());
    
    List<Child> children2 = p.getChildren();
    assertThat(children2, hasSize(2));
    
    Map<Long, Child> map2 = children2.stream().collect(
        Collectors.toMap(c -> c.getId(), Function.identity()));
    
    Child ch2 = map2.get(idChild2);
    assertThat(ch2, notNullValue());

    ch2.setName("Child2_Name_NEW");
    em.persist(pnew2);
    em.flush();
    
    TestTransaction.end();

    // start a new transaction - no need to commit it
    TestTransaction.start();
    
    Parent pfinal = em.find(Parent.class, idParent);

    assertThat(pfinal, notNullValue());
    
    List<Child> childrenfinal = p.getChildren();
    assertThat(childrenfinal, hasSize(2));
    
    Map<Long, Child> mapfinal = childrenfinal.stream().collect(
        Collectors.toMap(c -> c.getId(), Function.identity()));
    
    Child ch2final = mapfinal.get(idChild2);
    assertThat(ch2final, notNullValue());
    
    assertThat(ch2final.getName(), is("Child2_Name_NEW"));
    
  }
  
  @Test
  @Transactional
  public void testChildPersistOnUpdate() {
    // set the ids
    final Long idParent = 275L;
    final Long idChild1 = 1001L;
    final Long idChild2 = 1002L;    

    // flag the current tx for commit
    TestTransaction.flagForCommit();

    
    Parent p = new Parent(idParent, "Parent_Name");
    
    Child c1 = new Child(idChild1, "Child1_Name");
    c1.setParent(p);
    
    Child c2 = new Child(idChild2, "Child2_Name");
    c2.setParent(p);
    
    em.persist(p);
    em.flush();
    
    TestTransaction.end();
    
    em.clear();
    
    // start a new transaction
    TestTransaction.start();
    TestTransaction.flagForCommit();
    
    Parent pnew = em.find(Parent.class, idParent);
    
    assertThat(pnew, notNullValue());
    
    List<Child> children = p.getChildren();
    
    assertThat(children, hasSize(2));
    Map<Long, Child> map = children.stream().collect(
        Collectors.toMap(c -> c.getId(), Function.identity()));
    
    assertThat(map.get(idChild1), notNullValue());
    assertThat(map.get(idChild1).getName(), is("Child1_Name"));
    
    assertThat(map.get(idChild2), notNullValue());
    assertThat(map.get(idChild2).getName(), is("Child2_Name"));

    TestTransaction.end();
    
    em.clear();
    
    // start a new transaction
    TestTransaction.start();
    TestTransaction.flagForCommit();
    
    Child ch2 = em.find(Child.class, idChild2);
    assertThat(ch2, notNullValue());

    ch2.setName("Child2_Name_NEW");
    em.persist(ch2);
    em.flush();
    
    TestTransaction.end();

    // start a new transaction - no need to commit it
    TestTransaction.start();
    
    Parent pfinal = em.find(Parent.class, idParent);

    assertThat(pfinal, notNullValue());
    
    List<Child> childrenfinal = pfinal.getChildren();
    assertThat(childrenfinal, hasSize(2));
    
    Map<Long, Child> mapfinal = childrenfinal.stream().collect(
        Collectors.toMap(c -> c.getId(), Function.identity()));
    
    Child ch2final = mapfinal.get(idChild2);
    assertThat(ch2final, notNullValue());
    
    assertThat(ch2final.getName(), is("Child2_Name_NEW"));
    
  }
  
  @Test
  @Transactional
  public void testParentPersistOnUpdate() {
    // set the ids
    final Long idParent = 275L;
    final Long idChild1 = 1001L;
    final Long idChild2 = 1002L;   
    
    // flag the current tx for commit
    TestTransaction.flagForCommit();

    Parent p = new Parent(idParent, "Parent_Name");
    
    Child c1 = new Child(idChild1, "Child1_Name");
    c1.setParent(p);
    
    Child c2 = new Child(idChild2, "Child2_Name");
    c2.setParent(p);
    
    em.persist(p);
    em.flush();
    
    TestTransaction.end();
    
    em.clear();
    
    // start a new transaction
    TestTransaction.start();
    TestTransaction.flagForCommit();
    
    Parent pnew = em.find(Parent.class, idParent);
    
    assertThat(pnew, notNullValue());
    
    List<Child> children = p.getChildren();
    
    assertThat(children, hasSize(2));
    Map<Long, Child> map = children.stream().collect(
        Collectors.toMap(c -> c.getId(), Function.identity()));
    
    assertThat(map.get(idChild1), notNullValue());
    assertThat(map.get(idChild1).getName(), is("Child1_Name"));
    
    assertThat(map.get(idChild2), notNullValue());
    assertThat(map.get(idChild2).getName(), is("Child2_Name"));

    TestTransaction.end();
    
    em.clear();
    
    // start a new transaction
    TestTransaction.start();
    TestTransaction.flagForCommit();
    
    Child ch2 = em.find(Child.class, idChild2);
    assertThat(ch2, notNullValue());

    ch2.setName("Child2_Name_NEW");
    
    assertThat(ch2.getName(), is("Child2_Name_NEW"));
    
    Parent px = ch2.getParent();
    assertThat(px, notNullValue());
    assertThat(px.getId(), is(idParent));
    
    em.persist(px);
    em.flush();
    
    TestTransaction.end();

    // start a new transaction - no need to commit it
    TestTransaction.start();
    
    Parent pfinal = em.find(Parent.class, idParent);

    assertThat(pfinal, notNullValue());
    
    List<Child> childrenfinal = pfinal.getChildren();
    assertThat(childrenfinal, hasSize(2));
    
    Map<Long, Child> mapfinal = childrenfinal.stream().collect(
        Collectors.toMap(c -> c.getId(), Function.identity()));
    
    Child ch2final = mapfinal.get(idChild2);
    assertThat(ch2final, notNullValue());
    
    assertThat(ch2final.getName(), is("Child2_Name_NEW"));
    
  }
}
