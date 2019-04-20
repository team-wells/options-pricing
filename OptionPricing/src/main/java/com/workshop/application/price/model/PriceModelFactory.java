package com.workshop.application.price.model;

import com.workshop.application.datamodel.OptionType;

public final class PriceModelFactory {


    public static OptionPriceCalc getModel(String name, OptionType optionType) {
        switch (name) {
            case "jquant":
                return new JQuantOptionPriceCalc(optionType);
            default:
                return new MaygardOptionPriceCalc(optionType);
        }
    }

}
