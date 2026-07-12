package ru.murlov.dto;

public record CurrencyResponse (int id,
                                String code,
                                String name,
                                String sign) {
}
