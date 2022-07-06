package com.ubs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ubs.model.LegalEntity;

@Repository
public interface LegalEntityRepository extends JpaRepository<LegalEntity, Integer> {

}
