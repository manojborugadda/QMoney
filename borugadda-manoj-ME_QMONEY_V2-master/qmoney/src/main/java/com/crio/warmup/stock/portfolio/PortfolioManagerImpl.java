
package com.crio.warmup.stock.portfolio;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.lang.reflect.Array;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.crio.warmup.stock.quotes.StockQuotesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.print.DocFlavor.READER;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {

  private RestTemplate restTemplate;
  private StockQuotesService stockQuotesService;



  // Caution: Do not delete or modify the constructor, or else your build will break!
  // This is absolutely necessary for backward compatibility
  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public PortfolioManagerImpl(StockQuotesService stockQuotesService) {
    this.stockQuotesService = stockQuotesService;
  }

  public PortfolioManagerImpl(){

  }

  public PortfolioManagerImpl(StockQuotesService service, RestTemplate restTemplate2) {
      this.restTemplate = restTemplate2;
      this.stockQuotesService = service; 
    }



  //TODO: CRIO_TASK_MODULE_REFACTOR
  // 1. Now we want to convert our code into a module, so we will not call it from main anymore.
  //    Copy your code from Module#3 PortfolioManagerApplication#calculateAnnualizedReturn
  //    into #calculateAnnualizedReturn function here and ensure it follows the method signature.
  // 2. Logic to read Json file and convert them into Objects will not be required further as our
  //    clients will take care of it, going forward.

  // Note:
  // Make sure to exercise the tests inside PortfolioManagerTest using command below:
  // ./gradlew test --tests PortfolioManagerTest

  //CHECKSTYLE:OFF







  private Comparator<AnnualizedReturn> getComparator() {
    return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  }

  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Extract the logic to call Tiingo third-party APIs to a separate function.
  //  Remember to fill out the buildUri function and use that.


  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException, StockQuoteServiceException {
        // ObjectMapper objectMapper = getObjectMapper();
        // String uString = buildUri(symbol, from, to);
        // String result  = restTemplate.getForObject(uString,String.class);
        // return Arrays.asList(objectMapper.readValue(result,Candle[].class));
        return stockQuotesService.getStockQuote(symbol, from, to);
  }

  private ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }


  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
       String uriTemplate = "https:api.tiingo.com/tiingo/daily/"+symbol+"/prices?"
            + "startDate="+startDate.toString()+"&endDate="+endDate.toString()+"&token="+"4763dd824467fef66a5d6517b429545bbcc25998";
            return uriTemplate;
  }

  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades,
  LocalDate endDate)throws JsonMappingException,StockQuoteServiceException,JsonProcessingException  {
    List<AnnualizedReturn> annualizedReturns  = new ArrayList<AnnualizedReturn>();
    for(PortfolioTrade portfolioTrade : portfolioTrades) {
      List<Candle> list = getStockQuote(portfolioTrade.getSymbol(), portfolioTrade.getPurchaseDate(), endDate);
      AnnualizedReturn x = caluAnnualizedReturns(endDate, portfolioTrade,list.get(0).getOpen(),list.get(list.size()-1).getClose());
      annualizedReturns.add(x);
     
      // Collections.sort(annualizedReturns, getComparator());
    }
    return annualizedReturns.stream().sorted(getComparator()).collect(Collectors.toList());  
  }

  private AnnualizedReturn caluAnnualizedReturns(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice) {
        Double totalReturn = (sellPrice - buyPrice) / buyPrice;
        long daysBetween = ChronoUnit.DAYS.between(trade.getPurchaseDate(), endDate);
        double years = (double) daysBetween / 365.24;
        Double annualret = Math.pow(1 + totalReturn, 1 / years) - 1;
        return new AnnualizedReturn(trade.getSymbol(), annualret, totalReturn);
      
  }
  // public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
  //     PortfolioTrade trade, Double buyPrice, Double sellPrice) {
  //       //Calculate totalReturn = (sell_value - buy_value) / buy_value.
  //       double totalReturns = (sellPrice - buyPrice) / buyPrice;
  //       long totalDays = ChronoUnit.DAYS.between(trade.getPurchaseDate(), endDate);
  //       double total_num_years = (double)totalDays/365.24;
  //       double annualized_returns = Math.pow(1+totalReturns, (1/total_num_years)) - 1;
  //       return new AnnualizedReturn(trade.getSymbol(),annualized_returns,totalReturns); 
  // }


  // ¶TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Modify the function #getStockQuote and start delegating to calls to
  //  stockQuoteService provided via newly added constructor of the class.
  //  You also have a liberty to completely get rid of that function itself, however, make sure
  //  that you do not delete the #getStockQuote function.
  @Override
  public List<AnnualizedReturn> calculateAnnualizedReturnParallel( List<PortfolioTrade> portfolioTrades,LocalDate endDate, int numThreads) throws InterruptedException, StockQuoteServiceException {
      ExecutorService executor = Executors.newFixedThreadPool(numThreads);
      List<AnnualizedReturn> anreturns = new ArrayList<AnnualizedReturn>();
      List<Future<AnnualizedReturn>> list = new ArrayList<Future<AnnualizedReturn>>();
  
      for (PortfolioTrade symbol : portfolioTrades) {
        Callable<AnnualizedReturn> callable = new PortfolioCallable(symbol,endDate,this.stockQuotesService);
        Future<AnnualizedReturn> future = executor.submit(callable);
        list.add(future);
      }
  
      for (Future<AnnualizedReturn> fut : list) {
        try {
          anreturns.add(fut.get());
        } catch (ExecutionException e) {
          throw new StockQuoteServiceException("Execution exception");
        }
      }
      Collections.sort(anreturns, getComparator());
  
      executor.shutdown();
  
      return anreturns;  
    }


}
