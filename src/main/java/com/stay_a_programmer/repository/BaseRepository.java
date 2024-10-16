package com.stay_a_programmer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Long> {
    @Query("SELECT e FROM #{#entityName} e WHERE e.isDeleted = false")
    @Override
    List<T> findAll();

    @Query("SELECT e FROM #{#entityName} e WHERE e.isDeleted = false AND e.id = :id")
    @Override
    Optional<T> findById(@Param("id") Long id);
}
