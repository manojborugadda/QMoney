
package com.crio.warmup.stock;


import com.crio.warmup.stock.dto.*;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.crio.warmup.stock.portfolio.PortfolioManager;
import com.crio.warmup.stock.portfolio.PortfolioManagerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sound.midi.Track;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;


public class PortfolioManagerApplication {

  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Task:
  //       - Read the json file provided in the argument[0], The file is available in the classpath.
  //       - Go through all of the trades in the given file,
  //       - Prepare the list of all symbols a portfolio has.
  //       - if "trades.json" has trades like
  //         [{ "symbol": "MSFT"}, { "symbol": "AAPL"}, { "symbol": "GOOGL"}]
  //         Then you should return ["MSFT", "AAPL", "GOOGL"]
  //  Hints:
  //    1. Go through two functions provided - #resolveFileFromResources() and #getObjectMapper
  //       Check if they are of any help to you.
  //    2. Return the list of all symbols in the same order as provided in json.

  //  Note:
  //  1. There can be few unused imports, you will need to fix them to make the build pass.
  //  2. You can use "./gradlew build" to check if your code builds successfully.

  public static List<String> mainReadFile(String[] args) throws IOException, URISyntaxException {
    List<String> symbolss = new ArrayList<>();
    if(args.length > 0){
      File file = resolveFileFromResources(args[0]);
      byte[] byteArray = Files.readAllBytes(file.toPath());
      String content = new String(byteArray, "UTF8");

      ObjectMapper mapper = getObjectMapper();
      PortfolioTrade[] trades = mapper.readValue(content, PortfolioTrade[].class);

    for (int i = 0; i < trades.length; i++) {
      symbolss.add(trades[i].getSymbol());
    }
  }
    return symbolss;
}

 



  private static void printJsonObject(Object object) throws IOException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));
  }

  private static File resolveFileFromResources(String filename) throws URISyntaxException {
    return Paths.get(
        Thread.currentThread().getContextClassLoader().getResource(filename).toURI()).toFile();
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }


  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Follow the instructions provided in the task documentation and fill up the correct values for
  //  the variables provided. First value is provided for your reference.
  //  A. Put a breakpoint on the first line inside mainReadFile() which says
  //    return Collections.emptyList();
  //  B. Then Debug the test #mainReadFile provided in PortfoliomanagerApplicationTest.java
  //  following the instructions to run the test.
  //  Once you are able to run the test, perform following tasks and record the output as a
  //  String in the function below.
  //  Use this link to see how to evaluate expressions -
  //  https://code.visualstudio.com/docs/editor/debugging#_data-inspection
  //  1. evaluate the value of "args[0]" and set the value
  //     to the variable named valueOfArgument0 (This is implemented for your reference.)
  //  2. In the same window, evaluate the value of expression below and set it
  //  to resultOfResolveFilePathArgs0
  //     expression ==> resolveFileFromResources(args[0])
  //  3. In the same window, evaluate the value of expression below and set it
  //  to toStringOfObjectMapper.
  //  You might see some garbage numbers in the output. Dont worry, its expected.
  //    expression ==> getObjectMapper().toString()
  //  4. Now Go to the debug window and open stack trace. Put the name of the function you see at
  //  second place from top to variable functionNameFromTestFileInStackTrace
  //  5. In the same window, you will see the line number of the function in the stack trace window.
  //  assign the same to lineNumberFromTestFileInStackTrace
  //  Once you are done with above, just run the corresponding test and
  //  make sure its working as expected. use below command to do the same.
  //  ./gradlew test --tests PortfolioManagerApplicationTest.testDebugValues

  public static List<String> debugOutputs() {

     String valueOfArgument0 = "trades.json";
     String resultOfResolveFilePathArgs0 = "/home/crio-user/workspace/borugadda-manoj-ME_QMONEY_V2/qmoney/bin/main/trades.json";
     String toStringOfObjectMapper = "com.fasterxml.jackson.databind.ObjectMapper@5542c4ed";
     String functionNameFromTestFileInStackTrace = "PortfolioManagerApplicationTest.mainReadFile()";
     String lineNumberFromTestFileInStackTrace = "29";


    return Arrays.asList(new String[]{valueOfArgument0, resultOfResolveFilePathArgs0,
        toStringOfObjectMapper, functionNameFromTestFileInStackTrace,
        lineNumberFromTestFileInStackTrace});
  }


  // Note:
  // Remember to confirm that you are getting same results for annualized returns as in Module 3.
   // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.

  // TODO: CRIO_TASK_MODULE_REST_API
  //  Find out the closing price of each stock on the end_date and return the list
  //  of all symbols in ascending order by its close value on end date.

  // Note:
  // 1. You may have to register on Tiingo to get the api_token.
  // 2. Look at args parameter and the module instructions carefully.
  // 2. You can copy relevant code from #mainReadFile to parse the Json.
  // 3. Use RestTemplate#getForObject in order to call the API,
  //    and deserialize the results in List<Candle>
  public static List<String> mainReadQuotes(String[] args) throws IOException, URISyntaxException {
     
  // /**
  //  * Java doc for our understanding for later use
  //  * we have to call below API
  //  * https://api.tiingo.com/tiingo/daily/<ticker>/prices?startDate=2012-1-1&endDate=2016-1-1
  //  * 
  //  * think of "ticker" as symbol in portfolio trade
  //  * startDate as purchase date in portfolio trade 
  //  * endDate as arguments[1]
  //  * 
  //  * Use RestTemplate#getForObject in order to call the API,
  //  * refer this link for understanding https://spring.io/blog/2009/03/27/rest-in-spring-3-resttemplate
  //  * 
  //  
  List<String>symbols = new ArrayList<String>();
  File file  = resolveFileFromResources(args[0]);
  ObjectMapper objectMapper = getObjectMapper();
  List<PortfolioTrade> trades = Arrays.asList(objectMapper.readValue(file, PortfolioTrade[].class));
  List<TotalReturnsDto> totalReturnsDtoList = helper(args,trades);
  // Sort the stocks in ascending order of their closing price.
    Collections.sort(totalReturnsDtoList,TotalReturnsDto.comparator);
     for(TotalReturnsDto tDto : totalReturnsDtoList) {
      symbols.add(tDto.getSymbol());
    }
    return symbols;
  }

  public static List<TotalReturnsDto> helper(String[] args,List<PortfolioTrade>trades) throws IOException, URISyntaxException {
    RestTemplate restTemplate = new RestTemplate();
    List<TotalReturnsDto>tList  = new ArrayList<>();
    
    for(PortfolioTrade portfolioTrade : trades) {
      String URI = prepareUrl(portfolioTrade, LocalDate.parse(args[1]),"4763dd824467fef66a5d6517b429545bbcc25998");
      TiingoCandle[] tiingoCandles = restTemplate.getForObject(URI, TiingoCandle[].class);
      if(tiingoCandles != null) {
        tList.add(new TotalReturnsDto(portfolioTrade.getSymbol(), tiingoCandles[tiingoCandles.length-1].getClose()));
      }
    }
    return tList;
  }
// we have added in the module 3
  public static String getToken() {
    return "4763dd824467fef66a5d6517b429545bbcc25998";
  }











  // TODO:
  //  After refactor, make sure that the tests pass by using these two commands
  //  ./gradlew test --tests PortfolioManagerApplicationTest.readTradesFromJson
  //  ./gradlew test --tests PortfolioManagerApplicationTest.mainReadFile


  /*
   Read the content of JSON file using ObjectMapper
- Store it as a List
 - Return the List
 */
  public static List<PortfolioTrade> readTradesFromJson(String filename) throws IOException, URISyntaxException {
  List<PortfolioTrade>list = new ArrayList<PortfolioTrade>();
  File file  = resolveFileFromResources(filename);
  ObjectMapper objectMapper = getObjectMapper();
  PortfolioTrade[] trades = objectMapper.readValue(file, PortfolioTrade[].class);
  for(PortfolioTrade portfolioTrade : trades) {
    list.add(portfolioTrade);
  }
  return list;
  }


  // TODO:
  //  Build the Url using given parameters and use this function in your code to cann the API.
  public static String prepareUrl(PortfolioTrade trade, LocalDate endDate, String token) {
    String symbol = trade.getSymbol();
    LocalDate startDate =trade.getPurchaseDate();
    String URL = "https://api.tiingo.com/tiingo/daily/" + symbol
                + "/prices?startDate=" +startDate
                + "&endDate=" + endDate 
                + "&token=" +token;
     return URL;
  }

  // TODO:
  //  Ensure all tests are passing using below command
  //  ./gradlew test --tests ModuleThreeRefactorTest
  static Double getOpeningPriceOnStartDate(List<Candle> candles) {
    return candles.get(0).getOpen();
  }


  public static Double getClosingPriceOnEndDate(List<Candle> candles) {
    return candles.get(candles.size()-1).getClose();
  }


  public static List<Candle> fetchCandles(PortfolioTrade trade, LocalDate endDate, String token) {
    List<Candle> list = new ArrayList<Candle>();
    RestTemplate restTemplate = new RestTemplate();
    String Url = prepareUrl(trade,endDate,token);
    TiingoCandle[] tiingoCandles = restTemplate.getForObject(Url, TiingoCandle[].class);
    for( TiingoCandle x : tiingoCandles) {
      list.add(x);
    }
    return list;
  }

  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Now that you have the list of PortfolioTrade and their data, calculate annualized returns
  //  for the stocks provided in the Json.
  //  Use the function you just wrote #calculateAnnualizedReturns.
  //  Return the list of AnnualizedReturns sorted by annualizedReturns in descending order.

  // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.

  public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args)
      throws IOException, URISyntaxException {
        List<AnnualizedReturn>annualizedReturns = new ArrayList<AnnualizedReturn>();
        List<PortfolioTrade> portfolioTrades = readTradesFromJson(args[0]);
        LocalDate localDate =   LocalDate.parse(args[1]);
        for(PortfolioTrade pTrade : portfolioTrades) {
          List<Candle> candles = fetchCandles(pTrade, localDate, getToken());
          AnnualizedReturn aReturn = calculateAnnualizedReturns(localDate, pTrade, getOpeningPriceOnStartDate(candles), getClosingPriceOnEndDate(candles));
          annualizedReturns.add(aReturn);
        }
        //https://www.geeksforgeeks.org/stream-sorted-comparator-comparator-method-java/
     return annualizedReturns.stream()
                                    .sorted((a1,a2)->Double.compare(a2.getAnnualizedReturn(), a1.getAnnualizedReturn()))
                                    .collect(Collectors.toList());
  }

  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Return the populated list of AnnualizedReturn for all stocks.
  //  Annualized returns should be calculated in two steps:
  //   1. Calculate totalReturn = (sell_value - buy_value) / buy_value.
  //      1.1 Store the same as totalReturns
  //   2. Calculate extrapolated annualized returns by scaling the same in years span.
  //      The formula is:
  //      annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
  //      2.1 Store the same as annualized_returns
  //  Test the same using below specified command. The build should be successful.
  //     ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn

  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice) {
        //Calculate totalReturn = (sell_value - buy_value) / buy_value.
        double totalReturns = (sellPrice - buyPrice) / buyPrice;
        long totalDays = ChronoUnit.DAYS.between(trade.getPurchaseDate(), endDate);
        double total_num_years = (double)totalDays/365.24;
        double annualized_returns = Math.pow(1+totalReturns, (1/total_num_years)) - 1;
        return new AnnualizedReturn(trade.getSymbol(),annualized_returns,totalReturns); 
  }

























  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Once you are done with the implementation inside PortfolioManagerImpl and
  //  PortfolioManagerFactory, create PortfolioManager using PortfolioManagerFactory.
  //  Refer to the code from previous modules to get the List<PortfolioTrades> and endDate, and
  //  call the newly implemented method in PortfolioManager to calculate the annualized returns.

  // Note:
  // Remember to confirm that you are getting same results for annualized returns as in Module 3.

  public static List<AnnualizedReturn> mainCalculateReturnsAfterRefactor(String[] args)
      throws Exception {
       String file = args[0];
       LocalDate endDate = LocalDate.parse(args[1]);
        
       String contents = readFileAsString(file);
       ObjectMapper objectMapper = getObjectMapper();
       RestTemplate rest = new RestTemplate();
       PortfolioTrade[] portfolioTrades = objectMapper.readValue(contents, PortfolioTrade[].class);
       PortfolioManager portfolioManager = PortfolioManagerFactory.getPortfolioManager(rest);
       return portfolioManager.calculateAnnualizedReturn(Arrays.asList(portfolioTrades), endDate);
  }


  private static String readFileAsString(String file) throws IOException {
    String fileString = new String(Files.readAllBytes((Paths.get(file))));
    return fileString;
  }





  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());

    printJsonObject(mainReadFile(args));


    printJsonObject(mainReadQuotes(args));



    printJsonObject(mainCalculateSingleReturn(args));

    printJsonObject(mainCalculateReturnsAfterRefactor(args));
  }
}



//   }
// }

