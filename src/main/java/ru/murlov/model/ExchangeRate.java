package ru.murlov.model;

import java.math.BigDecimal;

public class ExchangeRate {

    private int id;
    private Currency base_currency;
    private Currency target_currency;
    private BigDecimal rate;

    public ExchangeRate(int id, Currency base_currency, Currency target_currency, BigDecimal rate) {
        this.id = id;
        this.base_currency = base_currency;
        this.target_currency = target_currency;
        this.rate = rate;
    }

    public ExchangeRate(Currency base_currency, Currency target_currency, BigDecimal rate) {
        this.base_currency = base_currency;
        this.target_currency = target_currency;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Currency getBase_currency() {
        return base_currency;
    }

    public void setBase_currency(Currency base_currency) {
        this.base_currency = base_currency;
    }

    public Currency getTarget_currency() {
        return target_currency;
    }

    public void setTarget_currency(Currency target_currency) {
        this.target_currency = target_currency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

}
