package com.ubs.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.ubs.model.ErrorWrapper;

@Service
public interface ErrorService {
	public <T> ErrorWrapper<T> wrapError(T trade, Errors errors);
}
