package com.workshop.application.price.model;

public interface OptionPriceCalc {

	double getOptionPrice(double spot, double strike, double iv, double time,double ir);
	double getDelta();
	double getGamma();
	double getTheta();
	double getRho();
	double getVega();
}
