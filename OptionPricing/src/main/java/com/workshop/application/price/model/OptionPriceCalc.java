package com.workshop.application.price.model;

public interface OptionPriceCalc {

	double getOptionPrice(double spot, double strike, double iv, String maturity,double ir);
	double getDelta();
	double getGamma();
	double getTheta();
	double getRho();
	double getVega();
}
