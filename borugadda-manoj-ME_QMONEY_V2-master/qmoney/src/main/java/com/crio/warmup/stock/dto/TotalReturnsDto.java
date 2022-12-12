
package com.crio.warmup.stock.dto;

import java.util.Comparator;

public class TotalReturnsDto {

  private String symbol;
  private Double closingPrice;

  public TotalReturnsDto(String symbol, Double closingPrice) {
    this.symbol = symbol;
    this.closingPrice = closingPrice;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public Double getClosingPrice() {
    return closingPrice;
  }

  public void setClosingPrice(Double closingPrice) {
    this.closingPrice = closingPrice;
  }

  public static Comparator<TotalReturnsDto>comparator = new Comparator<TotalReturnsDto>() {         
    @Override
    public int compare(TotalReturnsDto arg0, TotalReturnsDto arg1) {
      return (int)(arg0.getClosingPrice().compareTo(arg1.getClosingPrice()));
    }     
  };   
}
