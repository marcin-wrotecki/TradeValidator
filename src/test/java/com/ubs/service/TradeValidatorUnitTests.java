package com.ubs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubs.config.ValidationProperties;
import com.ubs.model.Counterparty;
import com.ubs.model.IsoCode;
import com.ubs.model.LegalEntity;
import com.ubs.model.Trade;
import com.ubs.repository.CounterpartyRepository;
import com.ubs.repository.IsoCodeRepository;
import com.ubs.repository.LegalEntityRepository;

public class TradeValidatorUnitTests {

	private final static int ISO_LENGTH = 3;
	static ValidationProperties validationProperties;
	static CounterpartyRepository counterpartyRepository;
	static IsoCodeRepository isoCodeRepository;
	static LegalEntityRepository legalEntityRepository;
	static ObjectMapper objectMapper;
	static TradeValidator tradeValidator;

	@BeforeAll
	static void init() {
		objectMapper = new ObjectMapper();
		validationProperties = spy(ValidationProperties.class);
		counterpartyRepository = mock(CounterpartyRepository.class);
		isoCodeRepository = mock(IsoCodeRepository.class);
		legalEntityRepository = mock(LegalEntityRepository.class);
		tradeValidator = new TradeValidator(validationProperties, counterpartyRepository, isoCodeRepository,
				legalEntityRepository);

		when(validationProperties.getIsoCodeLength()).thenReturn(ISO_LENGTH);
		when(validationProperties.getNonWorkingDays()).thenReturn(Collections.EMPTY_LIST);

		Counterparty yoda1 = new Counterparty();
		Counterparty yoda2 = new Counterparty();
		yoda1.setCounterparty("YODA1");
		yoda2.setCounterparty("YODA2");
		when(counterpartyRepository.findAll()).thenReturn(List.of(yoda1, yoda2));

		LegalEntity legalEntity = new LegalEntity();
		legalEntity.setEntity("UBS AG");
		when(legalEntityRepository.findAll()).thenReturn(List.of(legalEntity));

		IsoCode euro = new IsoCode();
		IsoCode usd = new IsoCode();
		euro.setCode("EUR");
		usd.setCode("USD");
		when(isoCodeRepository.findAll()).thenReturn(List.of(euro, usd));
	}

	@Test
	void shouldNotModifyErrorsWhenJsonIsValid() throws Exception {
		// given
		Trade trade = Trade.builder().customer("YODA1").ccyPair("EURUSD").type("Spot").direction("BUY")
				.tradeDate(LocalDate.of(2020, 8, 11)).amount1(1000000.00).amount2(1120000.00).rate(1.12)
				.deliveryDate(LocalDate.of(2020, 8, 12)).legalEntity("UBS AG").trader("Josef Schoenberger").build();
		Errors errors = new BindException(trade, "trade");
		// when
		tradeValidator.validate(trade, errors);
		// then
		assertThat(errors.getAllErrors().size()).isEqualTo(0);
	}

	@Test
	void shouldAddErrorsWhenDeliveryDataIsNotValid() throws Exception {
		// given
		Trade trade = Trade.builder().customer("YODA1").ccyPair("EURUSD").type("Spot").direction("BUY")
				.tradeDate(LocalDate.of(2020, 8, 11)).amount1(1000000.00).amount2(1120000.00).rate(1.12)
				.deliveryDate(LocalDate.of(2020, 8, 14)).legalEntity("UBS AG").trader("Josef Schoenberger").build();
		Errors errors = new BindException(trade, "trade");
		// when
		tradeValidator.validate(trade, errors);
		// then
		assertThat(errors.getAllErrors().size()).isEqualTo(1);
		assertThat(errors.getFieldError("deliveryDate").getCode())
				.isEqualTo("For spot trade delivery date should be max 2 business days after trade date");
	}
}
