package bcs.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuotesRequest {

	private List<Stock> stocks = new ArrayList<>();

	@Data
	public static class Stock {
		private String symbol;
		private Integer volume;
	}
}
