package com.gpaglia.springlock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.gpaglia.springlock.entities.Parent;
import com.gpaglia.springlock.repositories.ParentRepository;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.transaction.TestTransaction;

  
/**
 * CascadeJpaTest.
 */
@SpringBootTest()
public class SpringLockTest {

  @SuppressWarnings("unused")
  private static final Logger LOGGER = LoggerFactory.getLogger(SpringLockTest.class);

  @Autowired
  private ParentRepository parentRepo;

  @PersistenceContext
  private EntityManager em;
  
  @Test
  @Transactional
  public void forceIncrementWithEmOptimisticTest() {

    // flag the current tx for commit
    TestTransaction.flagForCommit();

    // set the id
    Long id = 275L;
    
    // build the entity
    Parent p0 = new Parent(id, "Parent_Name");

    assertThat(p0.getVersion(), is(nullValue()));

    // persist it 
    em.persist(p0);
    em.flush();

    // Version is 0 as expected
    assertThat(p0.getVersion(), is(0L));

    // commit the transaction
    TestTransaction.end();

    // clear the persistence context
    em.clear();

    // start a new transaction
    TestTransaction.start();
    TestTransaction.flagForCommit();

    // get the object again from DB, requiring the lock
    Parent p1 = em.find(Parent.class, id, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
    assertThat(p1, notNullValue());

    // version has been incremented as expected
    assertThat(p1.getVersion(), is(0L));

    // flush and clear the context
    em.flush();

    // Commit - this should icrement the version
    TestTransaction.end();

    em.clear();

    // start a new transaction
    TestTransaction.start();
    TestTransaction.flagForCommit();

    // get from DB without locks
    Parent p2 = em.find(Parent.class, id);
    assertThat(p2, notNullValue());

    // version has not been incremented this time, as expected
    assertThat(p2.getVersion(), is(1L));
    
  }

  @Test
  @Transactional
  public void forceIncrementWithEmPessimisticTest() {

    // set the id
    Long id = 278L;
    
    // build the entity
    Parent p0 = new Parent(id, "Parent_Name");

    assertThat(p0.getVersion(), is(nullValue()));

    // persist it 
    em.persist(p0);
    em.flush();

    // Version is 0 as expected
    assertThat(p0.getVersion(), is(0L));

    // clear the persistence context
    em.clear();

    // get the object again from DB, requiring the lock
    Parent p1 = em.find(Parent.class, id, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
    assertThat(p1, notNullValue());

    // version has been incremented as expected
    assertThat(p1.getVersion(), is(1L));

    // flush and clear the context
    em.flush();
    em.clear();

    // get from DB without locks
    Parent p2 = em.find(Parent.class, id);
    assertThat(p2, notNullValue());

    // version has not been incremented this time, as expected
    assertThat(p2.getVersion(), is(1L));
    
  }

  @Test
  @Transactional
  public void forceIncrementWithRepoQueryOptimisticTest() {

    // flag the current tx for commit
    TestTransaction.flagForCommit();

    // set the id
    Long id = 279L;
    
    // build the entity
    Parent p0 = new Parent(id, "Parent_Name");

    assertThat(p0.getVersion(), is(nullValue()));

    // persist it 
    em.persist(p0);
    em.flush();

    // Version is 0 as expected
    assertThat(p0.getVersion(), is(0L));

    // commit the transaction
    TestTransaction.end();

    // clear the persistence context
    em.clear();

    // start a new transaction
    TestTransaction.start();
    TestTransaction.flagForCommit();

    // get the object again from DB, requiring the lock
    // this time using the repository with the @Lock annotation
    // and a custom query

    Optional<Parent> popt = parentRepo.optimisticFindById(id);
    assertThat(popt.isPresent(), is(true));
    Parent p1 = popt.get();

    // Version still not incremented as tx not yet committed
    assertThat(p1.getVersion(), is(0L));

    Parent p2 = parentRepo.saveAndFlush(p1);
    // still version not incremented....
    assertThat(p2.getVersion(), is(0L));

    // commit
    TestTransaction.end();

    // Version is now incremended after commit
    assertThat(p2.getVersion(), is(1L));

    // start a new transaction
    TestTransaction.start();
    TestTransaction.flagForCommit();

    // get the object again from DB, by em to exclude any locks
    Parent p3 = em.find(Parent.class, id);

    // the version is again 1L as expected.
    assertThat(p3, is(notNullValue()));
    assertThat(p3.getVersion(), is(1L));

  }

  @Test
  @Transactional
  public void forceIncrementWithRepoQueryPessimisticTest() {

    // set the id
    Long id = 280L;
    
    // build the entity
    Parent p0 = new Parent(id, "Parent_Name");

    assertThat(p0.getVersion(), is(nullValue()));

    // persist it 
    em.persist(p0);
    em.flush();

    // Version is 0 as expected
    assertThat(p0.getVersion(), is(0L));

    // clear the persistence context
    em.clear();

    // get the object again from DB, requiring the lock
    // this time using the repository with the @Lock annotation
    // and a custom query

    Optional<Parent> popt = parentRepo.pessimisticFindById(id);
    assertThat(popt.isPresent(), is(true));
    Parent p1 = popt.get();

    // The version ahs been indeed incremented, so he PESSIMISTIC_FORCE_INCREMENT
    // works as expected.
    assertThat(p1.getVersion(), is(1L));

    Parent p2 = parentRepo.saveAndFlush(p1);

    // also the saved entity  has version  incremented
    // and  @Lock annotation was correctly considered..
    assertThat(p2.getVersion(), is(1L));

    
  }

  @Test
  @Transactional
  public void forceIncrementWithRepoStdTest() {

    // flag the current tx for commit
    TestTransaction.flagForCommit();

    // set the id
    Long id = 281L;
    
    // build the entity
    Parent p0 = new Parent(id, "Parent_Name");

    assertThat(p0.getVersion(), is(nullValue()));

    // persist it 
    em.persist(p0);
    em.flush();

    // Version is 0 as expected
    assertThat(p0.getVersion(), is(0L));

    // commit the transaction
    TestTransaction.end();

    // clear the persistence context
    em.clear();

    // start a new transaction
    TestTransaction.start();
    TestTransaction.flagForCommit();

    // get the object again from DB, requiring the lock
    // this time using the repository with the @Lock annotation
    // and a custom query

    Optional<Parent> popt = parentRepo.findById(id);
    assertThat(popt.isPresent(), is(true));
    Parent p1 = popt.get();

    // Version still not incremented as tx not yet committed
    assertThat(p1.getVersion(), is(0L));

    Parent p2 = parentRepo.saveAndFlush(p1);
    // still version not incremented....
    assertThat(p2.getVersion(), is(0L));

    // commit
    TestTransaction.end();

    // Version is now incremended after commit
    assertThat(p2.getVersion(), is(1L));

    // start a new transaction
    TestTransaction.start();
    TestTransaction.flagForCommit();

    // get the object again from DB, by em to exclude any locks
    Parent p3 = em.find(Parent.class, id);

    // the version is again 1L as expected.
    assertThat(p3, is(notNullValue()));
    assertThat(p3.getVersion(), is(1L));

    
  }
  
}