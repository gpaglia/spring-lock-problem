package com.gpaglia.springlock.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gpaglia.springlock.entities.Child;

public interface ChildRepository extends JpaRepository<Child, Long>{

}
