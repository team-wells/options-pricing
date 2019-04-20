package com.workshop.application.datamodel;

import java.util.List;

public class ComputePriceRequest {
	private String model;
	private double spotPrice;
	private double interestRate;
	private List<OptionPriceRequest> requests;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public List<OptionPriceRequest> getRequests() {
		return requests;
	}

	public void setRequests(List<OptionPriceRequest> requests) {
		this.requests = requests;
	}

	public double getSpotPrice() {
		return spotPrice;
	}

	public void setSpotPrice(double spotPrice) {
		this.spotPrice = spotPrice;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

}
