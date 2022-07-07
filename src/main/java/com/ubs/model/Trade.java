package com.ubs.model;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trade {

	@NotBlank
	@NotNull
	private String customer;

	@NotBlank
	@NotNull
	@Size(min = 3, max = 6)
	private String ccyPair;

	@NotBlank
	@NotNull
	private String type;

	@NotBlank
	@NotNull
	private String direction;

	@NotNull
	private LocalDate tradeDate;

	@NotNull
	private double amount1;

	@NotNull
	private double amount2;

	@NotNull
	private double rate;

	@NotNull
	@JsonProperty("valueDate")
	@JsonAlias("deliveryDate")
	private LocalDate deliveryDate;

	@NotBlank
	@NotNull
	private String legalEntity;

	@NotBlank
	@NotNull
	private String trader;
}
