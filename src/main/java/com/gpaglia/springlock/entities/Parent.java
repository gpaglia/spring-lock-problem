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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Parent.
 */

@Entity
@Table(name = "PARENTS")
@Access(AccessType.FIELD)
public class Parent {
  @Id
  private Long id;

  @Version
  private Long version;

  @Column(length = 32)
  private String name;
  
  @OneToMany(
      fetch = FetchType.LAZY, 
      mappedBy = "parent", 
      cascade = CascadeType.ALL, 
      orphanRemoval = true)
  private List<Child> children = new ArrayList<>();

  Parent() {}

  public Parent(Long id, String name) {
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
   * Get the children.
   * 
   * @return the children.
   */
  public List<Child> getChildren() {
    return children;
  }
  
  public void addChild(Child c) {
    children.add(c);
    c.setParentLocal(this);
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
    Parent other = (Parent) obj;
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