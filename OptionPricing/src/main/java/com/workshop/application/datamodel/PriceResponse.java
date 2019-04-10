package com.workshop.application.datamodel;

public class PriceResponse {

	private OptionType type;
	private double price;
	private double delta;
	private double gamma;
	private double theta;
	private double rho;
	private double vega;
	private double strikePrice;
	private String expireDate;

	public PriceResponse(double price, OptionType type, double strikePrice, String expireDate) {
		this.price = price;
		this.type = type;
		this.strikePrice = strikePrice;
		this.expireDate = expireDate;
	}

	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}

	public double getGamma() {
		return gamma;
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
	}

	public double getTheta() {
		return theta;
	}

	public void setTheta(double theta) {
		this.theta = theta;
	}

	public double getRho() {
		return rho;
	}

	public void setRho(double rho) {
		this.rho = rho;
	}

	public double getVega() {
		return vega;
	}

	public void setVega(double vega) {
		this.vega = vega;
	}

	public OptionType getType() {
		return type;
	}

	public double getPrice() {
		return price;
	}

	public double getStrikePrice() {
		return strikePrice;
	}

	public String getExpireDate() {
		return expireDate;
	}

}
