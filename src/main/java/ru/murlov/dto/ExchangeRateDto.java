package ru.murlov.dto;

import ru.murlov.model.Currency;

public class ExchangeRateDto {

    private Long id;
    private Currency base_currency;
    private Currency target_currency;
    private float rate;

    public ExchangeRateDto(Long id, Currency base_currency, Currency target_currency, float rate) {
        this.id = id;
        this.base_currency = base_currency;
        this.target_currency = target_currency;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
