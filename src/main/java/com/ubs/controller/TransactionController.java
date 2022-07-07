package com.ubs.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ubs.config.ValidationProperties;
import com.ubs.model.OptionTrade;
import com.ubs.model.Trade;
import com.ubs.service.ValidateService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class TransactionController {

	private ValidateService validateService;
	private ValidationProperties validationProperties;

	public TransactionController(ValidateService validateService, ValidationProperties validationProperties) {
		this.validateService = validateService;
		this.validationProperties = validationProperties;
	}

	@ApiOperation(value = "Validates FX Spot or FX Forward trade", notes = "Provide input as JSON")
	@PostMapping(value = "/trade/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateTrade(@ApiParam(value = "trade in JSON form") @RequestBody @Valid Trade trade,
			Errors errors) {
		if (validationProperties.OPTION_TYPE.equalsIgnoreCase(trade.getType())) {
			return ResponseEntity.badRequest().body("Wrong endpoint for option trades");
		}
		return validateService.validate(trade, errors);
	}

	@ApiOperation(value = "Validates collection of FX Spot or FX Forward trades", notes = "Provide input as JSON array")
	@PostMapping(value = "/trades/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateTrades(
			@ApiParam(value = "Collection of trades in JSON form") @RequestBody List<@Valid Trade> trades) {
		return validateService.validate(trades);
	}

	@ApiOperation(value = "Validates FX Option trade", notes = "Provide input as JSON ")
	@PostMapping(value = "/trade/option/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateTrade(
			@ApiParam(value = "trade in JSON form") @RequestBody @Valid OptionTrade trade, Errors errors) {
		return validateService.validate(trade, errors);
	}

	@ApiOperation(value = "Validates collection of FX Option trades", notes = "Provide input as JSON array")
	@PostMapping(value = "/trades/option/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateOptionTrades(
			@ApiParam(value = "Collection of trades in JSON form") @RequestBody List<@Valid OptionTrade> trades) {
		return validateService.validate(trades);
	}

}
