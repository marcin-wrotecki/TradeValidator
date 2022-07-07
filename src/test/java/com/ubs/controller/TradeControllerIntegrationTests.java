package com.ubs.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubs.model.ErrorWrapper;
import com.ubs.model.Trade;
import com.ubs.service.ErrorService;

@SpringBootTest
@AutoConfigureMockMvc
public class TradeControllerIntegrationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	ErrorService errorService;

	@Test
	void shouldReturnMessageWhenJsonIsValid() throws Exception {
		// given
		Trade trade = Trade.builder().customer("YODA1").ccyPair("EURUSD").type("Spot").direction("BUY")
				.tradeDate(LocalDate.of(2020, 8, 11)).amount1(1000000.00).amount2(1120000.00).rate(1.12)
				.deliveryDate(LocalDate.of(2020, 8, 12)).legalEntity("UBS AG").trader("Josef Schoenberger").build();
		String json = objectMapper.writeValueAsString(trade);
		// when
		// then
		this.mockMvc.perform(post("/trade/validate").contentType(MediaType.APPLICATION_JSON).content(json))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("JSON is valid")));
	}

	@Test
	void shouldReturnErrorMessageWhenSpotTradeRulesAreNotMeet() throws Exception {
		// given
		Trade trade = Trade.builder().customer("YODA1").ccyPair("EURUSD").type("Spot").direction("BUY")
				.tradeDate(LocalDate.of(2020, 8, 11)).amount1(1000000.00).amount2(1120000.00).rate(1.12)
				.deliveryDate(LocalDate.of(2020, 8, 14)).legalEntity("UBS AG").trader("Josef Schoenberger").build();
		String json = objectMapper.writeValueAsString(trade);
		Errors errors = new BindException(trade, "trade");
		errors.rejectValue("deliveryDate",
				"For spot trade delivery date should be max 2 business days after trade date");
		ErrorWrapper<Trade> errorWrapper = errorService.wrapError(trade, errors);
		String expectedResponse = objectMapper.writeValueAsString(errorWrapper);
		// when
		// then
		this.mockMvc.perform(post("/trade/validate").contentType(MediaType.APPLICATION_JSON).content(json))
				.andDo(print()).andExpect(status().isBadRequest())
				.andExpect(content().string(containsString(expectedResponse)));
	}

}
