package com.ubs.model;

import java.time.LocalDate;
import java.time.Month;

import lombok.Data;

@Data
public class PartialDate {
	private Month month;
	private int day;

	public boolean compareTo(LocalDate localDate) {
		return localDate.getMonth() == month && localDate.getDayOfMonth() == day;
	}
}
