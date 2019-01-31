package bcs.controller;

import bcs.model.QuotesRequest;
import bcs.model.QuotesResponse;
import bcs.service.IEXTradingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.zankowski.iextrading4j.api.stocks.BatchStocks;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "api", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class Controller {

	private IEXTradingService iexTradingService;

	@Autowired
	public Controller(IEXTradingService service) {
		this.iexTradingService = service;
	}

	@PostMapping(path = "quotes")
	public @ResponseBody QuotesResponse countStocks(@RequestBody QuotesRequest quotesRequest) {
		log.info("Received quotesRequest: {}", quotesRequest);
		List<QuotesRequest.Stock> stocks = quotesRequest.getStocks();
		List<String> symbols = stocks.stream().map(QuotesRequest.Stock::getSymbol).collect(Collectors.toList());
		Map<String, BatchStocks> stocksMap = this.iexTradingService.fetchQuoteBySymbols(symbols);
		QuotesResponse quotesResponse = prepareQuotesResponse(stocksMap, stocks);
		log.info("QuotesResponse body: {}", quotesResponse);
		return quotesResponse;
	}

	private QuotesResponse prepareQuotesResponse(Map<String, BatchStocks> stocksMap, List<QuotesRequest.Stock> stocks) {
		QuotesResponse quotesResponse = new QuotesResponse();
		Map<String, Integer> amounts = stocks.stream()
				.collect(Collectors.toMap(QuotesRequest.Stock::getSymbol, QuotesRequest.Stock::getVolume));
		List<QuotesResponse.Allocation> allocations = stocksMap.values().stream()
				.map(BatchStocks::getQuote)
				.map(q -> prepareAllocations.apply(q, amounts.get(q.getSymbol())))
				.collect(Collectors.toList());
		log.info("Allocations: {}", allocations);
		quotesResponse.addAllocations(allocations);
		return quotesResponse;
	}

	private BiFunction<Quote, Integer, QuotesResponse.Allocation> prepareAllocations = (quote, amount) ->
		new QuotesResponse.Allocation(
				quote.getSector(),
				quote.getLatestPrice().multiply(new BigDecimal(amount))
		);
}
