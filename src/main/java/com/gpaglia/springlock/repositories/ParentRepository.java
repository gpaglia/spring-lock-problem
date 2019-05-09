package com.gpaglia.springlock.repositories;

import com.gpaglia.springlock.entities.Parent;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

/**
 * ParentRepository.
 */
public interface ParentRepository extends JpaRepository<Parent, Long> {

  @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
  Optional<Parent> findById(Long id);

  @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
  @Query("select p from Parent p where p.id = ?1")
  Optional<Parent> optimisticFindById(Long id);

  @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
  @Query("select p from Parent p where p.id = ?1")
  Optional<Parent> pessimisticFindById(Long id);
  
}