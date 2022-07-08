package com.ubs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ubs.model.Style;

@Repository
public interface StyleRepository extends JpaRepository<Style, Integer> {

}
