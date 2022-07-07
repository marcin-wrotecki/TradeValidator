package com.ubs.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ubs.config.ValidationProperties;
import com.ubs.model.OptionTrade;
import com.ubs.repository.StyleRepository;

@Service
public class OptionTradeValidator implements Validator {

	private ValidationProperties validationProperties;
	private StyleRepository styleRepository;

	public OptionTradeValidator(ValidationProperties validationProperties, StyleRepository styleRepository) {
		super();
		this.validationProperties = validationProperties;
		this.styleRepository = styleRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return OptionTrade.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		OptionTrade trade = (OptionTrade) target;
		validateStyle(trade, errors);
		validateAmericanStyle(trade, errors);
		validateDate(trade, errors);
	}

	private void validateStyle(OptionTrade trade, Errors errors) {
		if (!styleRepository.findAll().stream().anyMatch(el -> el.getStyle().equalsIgnoreCase(trade.getStyle()))) {
			errors.rejectValue("style", "Style is not supported");
		}
	}

	private void validateAmericanStyle(OptionTrade trade, Errors errors) {
		if (!trade.getStyle().equalsIgnoreCase(validationProperties.AMERICAN_STYLE)) {
			return;
		}

		if (trade.getExerciseStartDate() == null) {
			errors.rejectValue("exerciseStartDate", "exerciseStartDate for American style cannot be null");
		}

		if (trade.getTradeDate() != null && !trade.getExerciseStartDate().isAfter(trade.getTradeDate())) {
			errors.rejectValue("exerciseStartDate", "exerciseStartDate for American style should be after trade date");
		}

		if (trade.getExpiryDate() != null && !trade.getExerciseStartDate().isBefore(trade.getExpiryDate())) {
			errors.rejectValue("exerciseStartDate",
					"exerciseStartDate for American style should be before expiry date");
		}
	}

	private void validateDate(OptionTrade trade, Errors errors) {
		if (trade.getDeliveryDate() == null) {
			return;
		}

		if (trade.getExpiryDate() != null && !trade.getDeliveryDate().isBefore(trade.getExpiryDate())) {
			errors.rejectValue("expiryDate", "expiryDate should be before delivery date");
		}

		if (trade.getPremiumDate() != null && !trade.getPremiumDate().isBefore(trade.getExpiryDate())) {
			errors.rejectValue("premiumDate", "premiumDate should be before delivery date");
		}
	}
}
