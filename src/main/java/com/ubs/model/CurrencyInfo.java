package com.ubs.model;

import java.util.List;

import lombok.Data;

@Data
public class CurrencyInfo {
	private String currency;
	private List<PartialDate> nonWorkingDay;
}
