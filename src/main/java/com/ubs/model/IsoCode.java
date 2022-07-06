package com.ubs.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="isocodes")
public class IsoCode {
	@Id
	private int id;
	private String code;
}
