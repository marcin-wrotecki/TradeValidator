package com.ubs.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.ubs.model.CurrencyInfo;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "validation")
public class ValidationProperties {
	public final String AMERICAN_STYLE = "American";
	public final String OPTION_TYPE = "VanillaOption";
	public final String SPOT_TYPE = "Spot";
	public final String FORWARD_TYPE = "Forward";
	public final int MAX_SPOT_TRADE_DURATION = 2;
	public final int MIN_FORWARD_TRADE_BREAK = 3;

	private int isoCodeLength;
	@Value("classpath:static/nonworkingDays.json")
	Resource resourceFile;

	private List<CurrencyInfo> nonWorkingDays = null;

	// simulation of real API that is usually payable
	@EventListener(ApplicationReadyEvent.class)
	private void loadNonworkingDays() {
		try (var content = resourceFile.getInputStream()) {
			ObjectMapper mapper = new ObjectMapper();
			CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class,
					CurrencyInfo.class);
			nonWorkingDays = mapper.readValue(content, collectionType);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
