package com.ubs.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import com.ubs.config.ValidationProperties;
import com.ubs.model.ErrorWrapper;
import com.ubs.model.Trade;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ValidateServiceImpl implements ValidateService {

	private TradeValidator tradeValidator;
	private OptionTradeValidator optionTradeValidator;
	private ErrorService errorService;
	private ValidationProperties validationProperties;
	private Validator validator;

	public ValidateServiceImpl(TradeValidator tradeValidator, OptionTradeValidator optionTradeValidator,
			ErrorService errorService, ValidationProperties validationProperties, Validator validator) {
		this.tradeValidator = tradeValidator;
		this.optionTradeValidator = optionTradeValidator;
		this.errorService = errorService;
		this.validationProperties = validationProperties;
		this.validator = validator;
	}

	@Override
	public ResponseEntity<?> validate(Trade trade, Errors errors) {
		runValidation(trade, errors);
		if (errors.hasErrors()) {
			var errorWrapper = errorService.wrapError(trade, errors);
			log.info(errorWrapper.toString());
			return ResponseEntity.badRequest().body(errorWrapper);
		}
		log.info("JSON is valid");
		return ResponseEntity.ok().body("JSON is valid");
	}

	@Override
	public ResponseEntity<?> validate(List<? extends Trade> trades) {
		List<ErrorWrapper<Trade>> errorWrappers = new LinkedList<>();
		int index = 0;
		for (var trade : trades) {
			// runs default validator
			Errors errors = getJavaValidationApiErrors(trade, index++);
			runValidation(trade, errors);
			if (errors.hasErrors()) {
				errorWrappers.add(errorService.wrapError(trade, errors));
			}
		}
		if (errorWrappers.size() > 0) {
			log.info(errorWrappers.toString());
			return ResponseEntity.badRequest().body(errorWrappers);
		}
		log.info("JSONs are valid");
		return ResponseEntity.ok().body("JSONs are valid");
	}

	private void runValidation(Trade trade, Errors errors) {
		tradeValidator.validate(trade, errors);
		validateOptionType(trade, errors);
	}

	private void validateOptionType(Trade trade, Errors errors) {
		if (validationProperties.OPTION_TYPE.equalsIgnoreCase(trade.getType())) {
			optionTradeValidator.validate(trade, errors);
		}
	}

	private Errors getJavaValidationApiErrors(Trade trade, int index) {
		Errors errors = new BindException(trade, "trade_" + index++);
		Set<ConstraintViolation<Trade>> violations = validator.validate(trade);
		for (var el : violations) {
			errors.rejectValue(el.getPropertyPath().toString(), el.getMessage());
		}
		return errors;
	}

}
