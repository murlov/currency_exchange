package ru.murlov.dto;

public record CurrencyCreateRequest(
        String code,
        String name,
        String sign
) {
}
