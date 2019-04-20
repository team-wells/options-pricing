package com.workshop.application.price.model;

import com.workshop.application.datamodel.OptionType;
import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.AmericanExercise;
import org.jquantlib.exercise.Exercise;
import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.Payoff;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.instruments.VanillaOption;
import org.jquantlib.pricingengines.vanilla.BaroneAdesiWhaleyApproximationEngine;
import org.jquantlib.processes.BlackScholesMertonProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;
import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Month;
import org.jquantlib.time.calendars.Target;


public class JQuantOptionPriceCalc implements OptionPriceCalc {

    private VanillaOption americanOption;
    private final Option.Type type;

    public JQuantOptionPriceCalc(OptionType type) {
        switch (type) {
            case PUT:
                this.type = Option.Type.Put;
                break;
            case CALL:
                this.type = Option.Type.Call;
                break;
            default:
                throw new RuntimeException("Invalid Option Type " + type);
        }

    }

    @Override
    public double getOptionPrice(double underlying, double strike, double volatility, String maturityStr, double riskFreeRate) {

        // set up dates
        final Calendar calendar = new Target();

        final Date settlementDate = Date.todaysDate();
        int dd = Integer.parseInt(maturityStr.substring(0,2));
        int mm = Integer.parseInt(maturityStr.substring(2,4));
        int yyyy = Integer.parseInt(maturityStr.substring(4));
        final Date maturity = new Date(dd, mm, yyyy);

        //final double underlying = 36.0;
        /*@Rate*/
        //final double riskFreeRate = 0.06;
        //final double volatility = 0.2;
        final double dividendYield = 0.00;

        final DayCounter dayCounter = new Actual365Fixed();

        // Define exercise for American Options
        final Exercise americanExercise = new AmericanExercise(settlementDate, maturity);
        // bootstrap the yield/dividend/volatility curves
        final Handle<Quote> underlyingH = new Handle<Quote>(new SimpleQuote(underlying));
        final Handle<YieldTermStructure> flatDividendTS = new Handle<YieldTermStructure>(new FlatForward(settlementDate, dividendYield, dayCounter));
        final Handle<YieldTermStructure> flatTermStructure = new Handle<YieldTermStructure>(new FlatForward(settlementDate, riskFreeRate, dayCounter));
        final Handle<BlackVolTermStructure> flatVolTS = new Handle<BlackVolTermStructure>(new BlackConstantVol(settlementDate, calendar, volatility, dayCounter));
        final Payoff payoff = new PlainVanillaPayoff(type, strike);

        final BlackScholesMertonProcess bsmProcess = new BlackScholesMertonProcess(underlyingH, flatDividendTS, flatTermStructure, flatVolTS);
        americanOption = new VanillaOption(payoff, americanExercise);
        // Barone-Adesi and Whaley approximation for American
        String method = "Barone-Adesi/Whaley";
        americanOption.setPricingEngine(new BaroneAdesiWhaleyApproximationEngine(bsmProcess));
        //System.out.printf(fmt, method, Double.NaN, Double.NaN, americanOption.NPV() );
        return americanOption.NPV();
    }

    @Override
    public double getDelta() {
        return americanOption.delta();
    }

    @Override
    public double getGamma() {
        return americanOption.gamma();
    }

    @Override
    public double getTheta() {
        return americanOption.theta();
    }

    @Override
    public double getRho() {
        return americanOption.rho();
    }

    @Override
    public double getVega() {
        return americanOption.vega();
    }
}
