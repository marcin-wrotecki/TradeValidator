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


@RestController
public class TransactionController {
	
	private ValidateService validateService;
	private ValidationProperties validationProperties;
	
	public TransactionController(ValidateService validateService, ValidationProperties validationProperties) {
		this.validateService = validateService;
		this.validationProperties = validationProperties;
	}

	
	@PostMapping(value = "/trade/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateTrade(@RequestBody @Valid Trade trade, Errors errors) {
		if(validationProperties.OPTION_TYPE.equalsIgnoreCase(trade.getType())){
			return ResponseEntity.badRequest().body("Wrong endpoint for option trades");
		}
		return validateService.validate(trade, errors);
	}
	
	@PostMapping(value = "/trades/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateTrades(@RequestBody List<@Valid Trade> trades) {
		return validateService.validate(trades);
	}

	@PostMapping(value = "/trade/option/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateTrade(@RequestBody @Valid OptionTrade trade, Errors errors) {
		return validateService.validate(trade, errors);
	}
	
	@PostMapping(value = "/trades/option/validate", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> validateOptionTrades(@RequestBody List<@Valid OptionTrade> trades) {
		return validateService.validate(trades);
	}
}
