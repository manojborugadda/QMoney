
package com.crio.warmup.stock.quotes;

import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.client.RestTemplate;

public class TiingoService implements StockQuotesService {


  private RestTemplate restTemplate;


  protected TiingoService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }


  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Implement getStockQuote method below that was also declared in the interface.

  // Note:
  // 1. You can move the code from PortfolioManagerImpl#getStockQuote inside newly created method.
  // 2. Run the tests using command below and make sure it passes.
  //    ./gradlew test --tests TiingoServiceTest


  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Write a method to create appropriate url to call the Tiingo API.

  @Override
  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to) throws StockQuoteServiceException, JsonMappingException, JsonProcessingException{
        //  ObjectMapper objmapper=new ObjectMapper();
        // objmapper.registerModule(new JavaTimeModule());

        // Candle[] candleobj=null;

        // String apiresponse=restTemplate.getForObject(buildUri(symbol, from, to), String.class);

        // try {
        //   candleobj=objmapper.readValue(apiresponse, TiingoCandle[].class);
        // } catch (JsonMappingException e) {
        //   e.printStackTrace();
        // } catch (JsonProcessingException e) {
        //   e.printStackTrace();
        // }

        // if(candleobj==null){
        //   return new ArrayList<>(); 
        // }
        // return Arrays.asList(candleobj);

          try{
            
            ObjectMapper objectMapper = getObjectMapper();
            String result = restTemplate.getForObject(buildUri(symbol, from, to),String.class);
            if(result == null || result.isEmpty()) {
              throw new StockQuoteServiceException("No response");
            }
            List<TiingoCandle>list = objectMapper.readValue(result,new TypeReference<ArrayList<TiingoCandle>>() {
            } );
            return new ArrayList<Candle>(list);

          } catch (RuntimeException exception) {
            exception.printStackTrace();
          } catch (JsonMappingException e) {
              e.printStackTrace();
            } catch (JsonProcessingException e) {
              e.printStackTrace();
            }

          return Collections.emptyList();
   }

  private ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  private String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
       String uriTemplate = "https:api.tiingo.com/tiingo/daily/"+symbol+"/prices?"
            + "startDate="+startDate.toString()+"&endDate="+endDate.toString()+"&token="+"4763dd824467fef66a5d6517b429545bbcc25998";
            return uriTemplate;
  }




  // TODO: CRIO_TASK_MODULE_EXCEPTIONS
  //  1. Update the method signature to match the signature change in the interface.
  //     Start throwing new StockQuoteServiceException when you get some invalid response from
  //     Tiingo, or if Tiingo returns empty results for whatever reason, or you encounter
  //     a runtime exception during Json parsing.
  //  2. Make sure that the exception propagates all the way from
  //     PortfolioManager#calculateAnnualisedReturns so that the external user's of our API
  //     are able to explicitly handle this exception upfront.

  //CHECKSTYLE:OFF



}
