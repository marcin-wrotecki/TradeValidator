package com.ubs.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="legalentities")
public class LegalEntity {
	@Id
	@GeneratedValue
	private int id;
	
	private String entity;
}
