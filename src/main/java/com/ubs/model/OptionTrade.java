package com.ubs.model;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class OptionTrade extends Trade {
	@NotNull
	@NotBlank
	private String style;

	@NotBlank
	@NotNull
	private String strategy;

	@NotNull
	private LocalDate expiryDate;

	@NotBlank
	@NotNull
	private String payCcy;

	@NotNull
	private double premium;

	@NotBlank
	@NotNull
	private String premiumCcy;

	@NotBlank
	@NotNull
	private String premiumType;

	@NotNull
	private LocalDate premiumDate;

	private LocalDate excerciseStartDate;

	@Override
	public String toString() {
		return "OptionTrade [customer=" + getCustomer() + ", ccyPair=" + getCcyPair() + ", type=" + getType()
				+ ", direction=" + getDirection() + ", tradeDate=" + getTradeDate() + ", amount1=" + getAmount1()
				+ ", amount2=" + getAmount2() + ", rate=" + getRate() + ", deliveryDate=" + getDeliveryDate()
				+ ", legalEntity=" + getLegalEntity() + ", trader=" + getTrader() + ", style=" + style + ", strategy="
				+ strategy + ", expiryDate=" + expiryDate + ", payCcy=" + payCcy + ", premium=" + premium
				+ ", premiumCcy=" + premiumCcy + ", premiumType=" + premiumType + ", premiumDate=" + premiumDate
				+ ", excerciseStartDate=" + excerciseStartDate + "]";
	}

}
