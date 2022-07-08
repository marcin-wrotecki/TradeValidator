package com.ubs.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ubs.config.ValidationProperties;
import com.ubs.model.CurrencyInfo;
import com.ubs.model.Trade;
import com.ubs.repository.CounterpartyRepository;
import com.ubs.repository.IsoCodeRepository;
import com.ubs.repository.LegalEntityRepository;

@Service
public class TradeValidator implements Validator {

	private ValidationProperties validationProperties;
	private CounterpartyRepository counterpartyRepository;
	private IsoCodeRepository isoCodeRepository;
	private LegalEntityRepository legalEntityRepository;

	public TradeValidator(ValidationProperties validationProperties, CounterpartyRepository counterpartyRepository,
			IsoCodeRepository isoCodeRepository, LegalEntityRepository legalEntityRepository) {
		this.validationProperties = validationProperties;
		this.counterpartyRepository = counterpartyRepository;
		this.isoCodeRepository = isoCodeRepository;
		this.legalEntityRepository = legalEntityRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Trade.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Trade trade = (Trade) target;
		// TODO it would be a good idea to add validation for amount
		validateDate(trade, errors);
		validateCustomer(trade, errors);
		validateEntity(trade, errors);
		validateCcyPair(trade, errors);
		validateSpotTrade(trade, errors);
		validateForwardTrade(trade, errors);
	}

	private void validateDate(Trade trade, Errors errors) {
		if (trade.getDeliveryDate() == null) {
			return;
		}
		if (trade.getTradeDate() != null && trade.getDeliveryDate().isBefore(trade.getTradeDate())) {
			errors.rejectValue("deliveryDate", "Delivery|Value date is before trade date");
		}

		DayOfWeek day = trade.getDeliveryDate().getDayOfWeek();
		if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
			errors.rejectValue("deliveryDate", "Delivery|Value date falls on weekend");
		}
		if (trade.getCcyPair().length() < validationProperties.getIsoCodeLength()) {
			return;
		}
		String currency1 = trade.getCcyPair().substring(0, validationProperties.getIsoCodeLength());
		String currency2 = trade.getCcyPair().substring(validationProperties.getIsoCodeLength());
		validateCurrencyNonWorkingDay(trade, errors, currency1);
		validateCurrencyNonWorkingDay(trade, errors, currency2);
	}

	private void validateCurrencyNonWorkingDay(Trade trade, Errors errors, String currency) {
		Optional<CurrencyInfo> currencyInfo = validationProperties.getNonWorkingDays().stream()
				.filter(el -> el.getCurrency().equalsIgnoreCase(currency)).findFirst();

		if (currencyInfo.isPresent() && currencyInfo.get().getNonWorkingDay().stream()
				.anyMatch(el -> el.compareTo(trade.getDeliveryDate()))) {
			errors.rejectValue("deliveryDate", "Delivery|Value date falls on nonworking day for " + currency);
		}

	}

	private void validateCustomer(Trade trade, Errors errors) {
		if (trade.getCustomer() != null && !trade.getCustomer().isBlank() && !counterpartyRepository.findAll().stream()
				.anyMatch(el -> el.getCounterparty().equalsIgnoreCase(trade.getCustomer()))) {
			errors.rejectValue("customer", "Counterparty is not supported");
		}
	}

	private void validateEntity(Trade trade, Errors errors) {
		if (!legalEntityRepository.findAll().stream()
				.anyMatch(el -> el.getEntity().equalsIgnoreCase(trade.getLegalEntity()))) {
			errors.rejectValue("legalEntity", "LegalEntity is not supported");
		}
	}

	private void validateCcyPair(Trade trade, Errors errors) {
		if (trade.getCcyPair() == null || trade.getCcyPair().length() < validationProperties.getIsoCodeLength()) {
			return;
		}
		String currency1 = trade.getCcyPair().substring(0, validationProperties.getIsoCodeLength());
		String currency2 = trade.getCcyPair().substring(validationProperties.getIsoCodeLength());
		if (!isoCodeRepository.findAll().stream().anyMatch(el -> el.getCode().equals(currency1))) {
			errors.rejectValue("ccyPair", currency1 + " is not supported");
		}
		if (!isoCodeRepository.findAll().stream().anyMatch(el -> el.getCode().equals(currency2))) {
			errors.rejectValue("ccyPair", currency2 + " is not supported");
		}
	}

	private void validateSpotTrade(Trade trade, Errors errors) {
		if (!validationProperties.SPOT_TYPE.equalsIgnoreCase(trade.getType())) {
			return;
		}
		if (trade.getTradeDate() == null || trade.getDeliveryDate() == null
				|| trade.getTradeDate().isAfter(trade.getDeliveryDate())) {
			return;
		}
		if (countBusinessDaysBetween(trade.getTradeDate(),
				trade.getDeliveryDate()) > validationProperties.MAX_SPOT_TRADE_DURATION) {
			errors.rejectValue("deliveryDate", "For spot trade delivery date should be max "
					+ validationProperties.MAX_SPOT_TRADE_DURATION + " business days after trade date");
		}

	}

	private void validateForwardTrade(Trade trade, Errors errors) {
		if (!validationProperties.FORWARD_TYPE.equalsIgnoreCase(trade.getType())) {
			return;
		}
		if (trade.getTradeDate() == null || trade.getDeliveryDate() == null
				|| trade.getTradeDate().isAfter(trade.getDeliveryDate())) {
			return;
		}
		if (countBusinessDaysBetween(trade.getTradeDate(),
				trade.getDeliveryDate()) < validationProperties.MIN_FORWARD_TRADE_BREAK) {
			errors.rejectValue("deliveryDate", "For forward trade delivery date should be minimum "
					+ validationProperties.MIN_FORWARD_TRADE_BREAK + " business days after trade date");
		}
	}

	private static long countBusinessDaysBetween(final LocalDate startDate, final LocalDate endDate) {
		Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
				|| date.getDayOfWeek() == DayOfWeek.SUNDAY;
		// TODO check holidays
		long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

		return Stream.iterate(startDate, date -> date.plusDays(1)).limit(daysBetween).filter(isWeekend.negate())
				.count();
	}

}
