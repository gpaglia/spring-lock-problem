package com.gpaglia.springlock.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Parent.
 */

@Entity
@Table(name = "CHILDREN")
@Access(AccessType.FIELD)
public class Child {
  @Id
  private Long id;

  @Version
  private Long version;

  @Column(length = 32)
  private String name;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "PARENT_ID")
  private Parent parent;
  
  @OneToMany(
      fetch = FetchType.LAZY, 
      mappedBy = "child", 
      cascade = CascadeType.ALL, 
      orphanRemoval = true)
  private List<GrandChild> grandChildren = new ArrayList<>();
  
  Child() {}

  public Child(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * Get the id.
   * 
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * Get the version.
   * 
   * @return the version
   */
  public Long getVersion() {
    return version;
  }

  /**
   * Get the name.
   * 
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name.
   * 
   * @param desc the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the parent.
   * 
   * @return the parent
   */
  public Parent getParent() {
    return parent;
  }
  
  /**
   * Set the parent and wire back this child in the parent.
   * 
   * @param p The parent to set.
   */
  public void setParent(Parent p) {
    if (parent != null) {
      if (parent.equals(p)) {
        return;
      } else {
        parent.getChildren().remove(this);
      }
    }
    
    if (p != null) {
      p.addChild(this);
    }

    setParentLocal(p);
  }

  /**
   * Set the parent without wiring.
   * 
   * @param p The parent to set
   */
  void setParentLocal(Parent p) {
    this.parent = p;
  }
  
  /**
   * Get the list of grand children.
   * 
   * @return The grand children.
   */
  public List<GrandChild> getGrandChildren() {
    return this.grandChildren;
  }
  
  public void addGrandChild(GrandChild gc) {
    grandChildren.add(gc);
    gc.setChildLocal(this);
    
  }

  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Child other = (Child) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }
}