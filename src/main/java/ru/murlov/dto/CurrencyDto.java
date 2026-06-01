package ru.murlov.dto;

public class CurrencyDto {
    private long id;
    private String code;
    private String name;
    private char sign;

    public CurrencyDto(long id) {
        this.id = id;
    }

    public CurrencyDto(String code, String name, char sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getSign() {
        return sign;
    }

    public void setSign(char sign) {
        this.sign = sign;
    }
}
