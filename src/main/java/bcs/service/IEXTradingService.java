package bcs.service;

import bcs.exception.IEXTradingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.BatchStocks;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.request.stocks.BatchMarketStocksRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.BatchStocksType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class IEXTradingService {

	private final IEXTradingClient iexTradingClient;

	@Autowired
	public IEXTradingService(IEXTradingClient client) {
		this.iexTradingClient = client;
	}

	public Map<String, BatchStocks> fetchQuoteBySymbols(List<String> symbols) {
		return fetchByTypeAndSymbols(Collections.singletonList(BatchStocksType.QUOTE), symbols);
	}

	private Map<String, BatchStocks> fetchByTypeAndSymbols(List<BatchStocksType> types, List<String> symbols) {
		log.info("Fetching {} for {}", types, symbols);
		BatchMarketStocksRequestBuilder builder = new BatchMarketStocksRequestBuilder();
		symbols.forEach(builder::withSymbol);
		types.forEach(builder::addType);
		try {
			Map<String, BatchStocks> stocks = iexTradingClient.executeRequest(builder.build());
			log.info("Received stocks: {}", stocks);
			return stocks;
		} catch (Exception e) {
			throw new IEXTradingException("Fetching failed: " + e.getMessage(), e);
		}
	}
}
