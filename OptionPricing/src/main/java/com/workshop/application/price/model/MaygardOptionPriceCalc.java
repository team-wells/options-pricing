package com.workshop.application.price.model;

import com.workshop.application.datamodel.OptionType;

public class MaygardOptionPriceCalc  implements OptionPriceCalc{
	
	private BackScholesMaygard bs = new BackScholesMaygard();
	private final OptionType type;
	
	public MaygardOptionPriceCalc(OptionType type) {
		this.type = type;
	}
	
	@Override
	public double getOptionPrice(double spot, double strike, double iv, double time, double ir) {
		bs.bscholEprice(spot, strike, iv, time, ir);
		return type == OptionType.CALL ? bs.getCalle() : bs.getPute();
	}
	
	@Override
	public double getDelta() {
		return type == OptionType.CALL ? bs.getDeltac() : bs.getDeltap();
	}
	
	@Override
	public double getGamma() {
		return bs.getGamma();
	}
	
	@Override
	public double getTheta() {
		return type == OptionType.CALL ? bs.getThetac() : bs.getThetac();
	}
	
	@Override
	public double getRho() {
		return type == OptionType.CALL ? bs.getRhoc() : bs.getRhop();
	}
	
	@Override
	public double getVega() {
		return bs.getVega();
	}
}
