package com.ubs.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.ubs.model.ErrorWrapper;

@Service
public class ErrorServiceImpl implements ErrorService {

	@Override
	public <T> ErrorWrapper<T> wrapError(T trade, Errors errors) {
		List<FieldError> list = errors.getFieldErrors();
		ErrorWrapper<T> errorWrapper = new ErrorWrapper<>();
		errorWrapper.setTrade(trade);
		Map<String, String> fieldErrors = errorWrapper.getFieldErrors();
		for (var el : list) {
			if (fieldErrors.containsKey(el.getField())) {
				String err = fieldErrors.get(el.getField()) + ", " + el.getCode();
				fieldErrors.put(el.getField(), err);
			} else {
				fieldErrors.put(el.getField(), el.getCode());
			}
		}
		return errorWrapper;
	}

}
