import bcs.Application;
import bcs.model.QuotesRequest;
import bcs.model.QuotesResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import pl.zankowski.iextrading4j.api.exception.IEXTradingException;
import pl.zankowski.iextrading4j.api.stocks.BatchStocks;
import pl.zankowski.iextrading4j.client.IEXTradingClient;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MainTest {

	@MockBean
	private IEXTradingClient iexTradingClient;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private TestRestTemplate restTemplate;

	private String jsonRequest = "{\"stocks\":[{\"symbol\":\"AAPL\",\"volume\":50},{\"symbol\":\"HOG\",\"volume\":10},{\"symbol\":\"MDSO\",\"volume\":1},{\"symbol\":\"IDRA\",\"volume\":1},{\"symbol\":\"MRSN\",\"volume\":1}]}";
	private String mockResponse = "{\"HOG\":{\"book\":null,\"chart\":[],\"company\":null,\"dividends\":[],\"earnings\":null,\"financials\":null,\"logo\":null,\"news\":null,\"ohlc\":null,\"peers\":[],\"previous\":null,\"price\":null,\"quote\":{\"symbol\":\"HOG\",\"companyName\":\"Harley-Davidson Inc.\",\"primaryExchange\":\"New York Stock Exchange\",\"sector\":\"Consumer Cyclical\",\"calculationPrice\":\"close\",\"open\":34.6,\"openTime\":1548858636856,\"close\":36.48,\"closeTime\":1548882210827,\"high\":36.61,\"low\":34.12,\"latestPrice\":36.48,\"latestSource\":\"Close\",\"latestTime\":\"January 30, 2019\",\"latestUpdate\":1548882210827,\"latestVolume\":6550235,\"iexRealtimePrice\":0,\"iexRealtimeSize\":0,\"iexLastUpdated\":0,\"delayedPrice\":36.48,\"delayedPriceTime\":1548882210827,\"extendedPrice\":36.48,\"extendedChange\":0,\"extendedChangePercent\":0,\"extendedPriceTime\":1548884711742,\"previousClose\":34.76,\"change\":1.72,\"changePercent\":0.04948,\"iexMarketPercent\":0,\"iexVolume\":0,\"avgTotalVolume\":2456805,\"iexBidPrice\":0,\"iexBidSize\":0,\"iexAskPrice\":0,\"iexAskSize\":0,\"marketCap\":5940138720,\"peRatio\":9.83,\"week52High\":51.11,\"week52Low\":31.36,\"ytdChange\":0.11990253521126756,\"bidPrice\":null,\"bidSize\":null,\"askPrice\":null,\"askSize\":null},\"relevant\":null,\"splits\":[],\"delayedQuote\":null,\"effectiveSpread\":[],\"thresholdSecurities\":[],\"shortInterest\":[],\"keyStats\":null,\"largestTrades\":[],\"venueVolume\":[]},\"AAPL\":{\"book\":null,\"chart\":[],\"company\":null,\"dividends\":[],\"earnings\":null,\"financials\":null,\"logo\":null,\"news\":null,\"ohlc\":null,\"peers\":[],\"previous\":null,\"price\":null,\"quote\":{\"symbol\":\"AAPL\",\"companyName\":\"Apple Inc.\",\"primaryExchange\":\"Nasdaq Global Select\",\"sector\":\"Technology\",\"calculationPrice\":\"close\",\"open\":163.22,\"openTime\":1548858600300,\"close\":165.25,\"closeTime\":1548882000563,\"high\":166.15,\"low\":160.23,\"latestPrice\":165.25,\"latestSource\":\"Close\",\"latestTime\":\"January 30, 2019\",\"latestUpdate\":1548882000563,\"latestVolume\":60722058,\"iexRealtimePrice\":null,\"iexRealtimeSize\":null,\"iexLastUpdated\":null,\"delayedPrice\":165.25,\"delayedPriceTime\":1548882000563,\"extendedPrice\":165.69,\"extendedChange\":0.44,\"extendedChangePercent\":0.00266,\"extendedPriceTime\":1548885598544,\"previousClose\":154.68,\"change\":10.57,\"changePercent\":0.06833,\"iexMarketPercent\":null,\"iexVolume\":null,\"avgTotalVolume\":43470394,\"iexBidPrice\":null,\"iexBidSize\":null,\"iexAskPrice\":null,\"iexAskSize\":null,\"marketCap\":781599945750,\"peRatio\":13.92,\"week52High\":233.47,\"week52Low\":142,\"ytdChange\":0.1147459067882473,\"bidPrice\":null,\"bidSize\":null,\"askPrice\":null,\"askSize\":null},\"relevant\":null,\"splits\":[],\"delayedQuote\":null,\"effectiveSpread\":[],\"thresholdSecurities\":[],\"shortInterest\":[],\"keyStats\":null,\"largestTrades\":[],\"venueVolume\":[]},\"MRSN\":{\"book\":null,\"chart\":[],\"company\":null,\"dividends\":[],\"earnings\":null,\"financials\":null,\"logo\":null,\"news\":null,\"ohlc\":null,\"peers\":[],\"previous\":null,\"price\":null,\"quote\":{\"symbol\":\"MRSN\",\"companyName\":\"Mersana Therapeutics Inc.\",\"primaryExchange\":\"Nasdaq Global Select\",\"sector\":\"Healthcare\",\"calculationPrice\":\"close\",\"open\":4.31,\"openTime\":1548858600459,\"close\":4.58,\"closeTime\":1548882000612,\"high\":4.6,\"low\":4.301,\"latestPrice\":4.58,\"latestSource\":\"Close\",\"latestTime\":\"January 30, 2019\",\"latestUpdate\":1548882000612,\"latestVolume\":55584,\"iexRealtimePrice\":null,\"iexRealtimeSize\":null,\"iexLastUpdated\":null,\"delayedPrice\":4.58,\"delayedPriceTime\":1548882000612,\"extendedPrice\":4.58,\"extendedChange\":0,\"extendedChangePercent\":0,\"extendedPriceTime\":1548885397753,\"previousClose\":4.27,\"change\":0.31,\"changePercent\":0.0726,\"iexMarketPercent\":null,\"iexVolume\":null,\"avgTotalVolume\":156854,\"iexBidPrice\":null,\"iexBidSize\":null,\"iexAskPrice\":null,\"iexAskSize\":null,\"marketCap\":106175722,\"peRatio\":null,\"week52High\":23.9565,\"week52Low\":2.845,\"ytdChange\":0.13524501160092817,\"bidPrice\":null,\"bidSize\":null,\"askPrice\":null,\"askSize\":null},\"relevant\":null,\"splits\":[],\"delayedQuote\":null,\"effectiveSpread\":[],\"thresholdSecurities\":[],\"shortInterest\":[],\"keyStats\":null,\"largestTrades\":[],\"venueVolume\":[]},\"MDSO\":{\"book\":null,\"chart\":[],\"company\":null,\"dividends\":[],\"earnings\":null,\"financials\":null,\"logo\":null,\"news\":null,\"ohlc\":null,\"peers\":[],\"previous\":null,\"price\":null,\"quote\":{\"symbol\":\"MDSO\",\"companyName\":\"Medidata Solutions Inc.\",\"primaryExchange\":\"Nasdaq Global Select\",\"sector\":\"Technology\",\"calculationPrice\":\"close\",\"open\":68.58,\"openTime\":1548858600258,\"close\":68.95,\"closeTime\":1548882000552,\"high\":69.11,\"low\":67.155,\"latestPrice\":68.95,\"latestSource\":\"Close\",\"latestTime\":\"January 30, 2019\",\"latestUpdate\":1548882000552,\"latestVolume\":359253,\"iexRealtimePrice\":null,\"iexRealtimeSize\":null,\"iexLastUpdated\":null,\"delayedPrice\":68.95,\"delayedPriceTime\":1548882000552,\"extendedPrice\":68.95,\"extendedChange\":0,\"extendedChangePercent\":0,\"extendedPriceTime\":1548885398606,\"previousClose\":68.2,\"change\":0.75,\"changePercent\":0.011,\"iexMarketPercent\":null,\"iexVolume\":null,\"avgTotalVolume\":514627,\"iexBidPrice\":null,\"iexBidSize\":null,\"iexAskPrice\":null,\"iexAskSize\":null,\"marketCap\":4219081665,\"peRatio\":71.82,\"week52High\":88.87,\"week52Low\":59.6,\"ytdChange\":0.052226215644820356,\"bidPrice\":null,\"bidSize\":null,\"askPrice\":null,\"askSize\":null},\"relevant\":null,\"splits\":[],\"delayedQuote\":null,\"effectiveSpread\":[],\"thresholdSecurities\":[],\"shortInterest\":[],\"keyStats\":null,\"largestTrades\":[],\"venueVolume\":[]},\"IDRA\":{\"book\":null,\"chart\":[],\"company\":null,\"dividends\":[],\"earnings\":null,\"financials\":null,\"logo\":null,\"news\":null,\"ohlc\":null,\"peers\":[],\"previous\":null,\"price\":null,\"quote\":{\"symbol\":\"IDRA\",\"companyName\":\"Idera Pharmaceuticals Inc.\",\"primaryExchange\":\"NASDAQ Capital Market\",\"sector\":\"Healthcare\",\"calculationPrice\":\"close\",\"open\":2.72,\"openTime\":1548858600675,\"close\":2.65,\"closeTime\":1548882000616,\"high\":2.9,\"low\":2.64,\"latestPrice\":2.65,\"latestSource\":\"Close\",\"latestTime\":\"January 30, 2019\",\"latestUpdate\":1548882000616,\"latestVolume\":444935,\"iexRealtimePrice\":0,\"iexRealtimeSize\":0,\"iexLastUpdated\":0,\"delayedPrice\":2.65,\"delayedPriceTime\":1548882000616,\"extendedPrice\":2.65,\"extendedChange\":0,\"extendedChangePercent\":0,\"extendedPriceTime\":1548885398380,\"previousClose\":2.72,\"change\":-0.07,\"changePercent\":-0.02574,\"iexMarketPercent\":0,\"iexVolume\":0,\"avgTotalVolume\":491058,\"iexBidPrice\":0,\"iexBidSize\":0,\"iexAskPrice\":0,\"iexAskSize\":0,\"marketCap\":72030805,\"peRatio\":-1.06,\"week52High\":17.12,\"week52Low\":2.2501,\"ytdChange\":-0.2728990909090909,\"bidPrice\":null,\"bidSize\":null,\"askPrice\":null,\"askSize\":null},\"relevant\":null,\"splits\":[],\"delayedQuote\":null,\"effectiveSpread\":[],\"thresholdSecurities\":[],\"shortInterest\":[],\"keyStats\":null,\"largestTrades\":[],\"venueVolume\":[]}}";
	private String jsonResponse = "{\"value\":8703.48,\"allocations\":[{\"sector\":\"Consumer Cyclical\",\"assetValue\":364.80,\"proportion\":0.04},{\"sector\":\"Technology\",\"assetValue\":8262.50,\"proportion\":0.95},{\"sector\":\"Healthcare\",\"assetValue\":4.58,\"proportion\":0.00},{\"sector\":\"Technology\",\"assetValue\":68.95,\"proportion\":0.01},{\"sector\":\"Healthcare\",\"assetValue\":2.65,\"proportion\":0.00}]}";

	@Test
	public void shouldReturnCorrectResponse() throws IOException {
		given(this.iexTradingClient.executeRequest(any()))
				.willReturn(mapper.readValue(mockResponse, new TypeReference<Map<String, BatchStocks>>(){}));
		QuotesResponse quotesResponse = this.restTemplate.postForObject("/api/quotes", mapper.readValue(jsonRequest, QuotesRequest.class), QuotesResponse.class);
		assertEquals(mapper.readValue(jsonResponse, QuotesResponse.class), quotesResponse);
	}

	@Test
	public void shouldReturnInternalServerError() throws IOException {
		given(this.iexTradingClient.executeRequest(any()))
				.willThrow(new IEXTradingException("Internal server error", 500));

		ResponseEntity<QuotesResponse> response = this.restTemplate.postForEntity("/api/quotes", mapper.readValue(jsonRequest, QuotesRequest.class), QuotesResponse.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}
}
