package com.workshop.application.datamodel;

public class PriceComparisonResponse {

	private double strikePrice;
	private String expireDate;
	private OptionType type;
	private double priceMG;
	private double priceJquant;
	
	public PriceComparisonResponse(double strikePrice, String expireDate, OptionType type) {
		this.strikePrice = strikePrice;
		this.expireDate = expireDate;
		this.type = type;
	}
	
	public double getPriceMG() {
		return priceMG;
	}
	public void setPriceMG(double priceMG) {
		this.priceMG = priceMG;
	}
	public double getPriceJquant() {
		return priceJquant;
	}
	public void setPriceJquant(double priceJquant) {
		this.priceJquant = priceJquant;
	}
	public double getStrikePrice() {
		return strikePrice;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public OptionType getType() {
		return type;
	}
	
	
}
