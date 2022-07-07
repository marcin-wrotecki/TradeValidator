package com.ubs.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.ubs.model.Trade;

@Service
public interface ValidateService {
	public ResponseEntity<?> validate(Trade trade, Errors errors);

	public ResponseEntity<?> validate(List<? extends Trade> trades);

}
