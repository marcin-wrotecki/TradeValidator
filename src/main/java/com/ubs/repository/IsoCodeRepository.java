package com.ubs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ubs.model.IsoCode;

@Repository
public interface IsoCodeRepository extends JpaRepository<IsoCode, Integer> {

}
