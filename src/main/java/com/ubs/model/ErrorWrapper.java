package com.ubs.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ErrorWrapper<T> {
	T trade;
	Map<String, String> fieldErrors = new HashMap<>();
}
