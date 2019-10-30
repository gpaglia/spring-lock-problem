package com.gpaglia.springlock.entities;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Parent.
 */

@Entity
@Table(name = "GRANDCHILDREN")
@Access(AccessType.FIELD)
public class GrandChild {
  @Id
  private Long id;

  @Version
  private Long version;

  @Column(length = 32)
  private String name;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Child child;
  
  GrandChild() {}

  public GrandChild(Long id, String name) {
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
   * Get the child.
   * 
   * @return the child
   */
  public Child getChild() {
    return child;
  }
  
  /**
   * Set the child and wire back this grandchild in the child.
   * 
   * @param c The child to set.
   */
  public void setChild(Child c) {
    if (child != null) {
      if (child.equals(c)) {
        return;
      } else {
        child.getGrandChildren().remove(this);
      }
    }
    
    if (c != null) {
      c.addGrandChild(this);
    }

    setChildLocal(c);
  }

  /**
   * Set the child without wiring.
   * 
   * @param c The child to set
   */
  void setChildLocal(Child c) {
    this.child = c;
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
    GrandChild other = (GrandChild) obj;
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