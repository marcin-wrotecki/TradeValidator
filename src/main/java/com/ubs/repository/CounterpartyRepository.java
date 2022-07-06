package com.ubs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ubs.model.Counterparty;

@Repository
public interface CounterpartyRepository extends JpaRepository<Counterparty, Integer> {

}
