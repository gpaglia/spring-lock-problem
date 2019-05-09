package com.gpaglia.springlock.entities;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
  private String desc;

  Parent() {}

  public Parent(Long id, String desc) {
    this.id = id;
    this.desc = desc;
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
   * Get the description.
   * 
   * @return the desc
   */
  public String getDesc() {
    return desc;
  }

  /**
   * Set the description.
   * 
   * @param desc the desc to set
   */
  public void setDesc(String desc) {
    this.desc = desc;
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