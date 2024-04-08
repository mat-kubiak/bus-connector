package com.github.mat_kubiak.tqs.bus_connector.boundary;

public class ExchangeRates {
    public double USD;
    public double PLN;
    public double CZK;

    public ExchangeRates(double usd, double pln, double czk) {
        this.USD = usd;
        this.PLN = pln;
        this.CZK = czk;
    }
}
