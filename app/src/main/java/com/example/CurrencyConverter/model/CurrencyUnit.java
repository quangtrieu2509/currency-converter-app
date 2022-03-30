package com.example.CurrencyConverter.model;

public class CurrencyUnit {
    private String code;
    private int name;
    private int symbol;
    private int flag;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getSymbol() {
        return symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public CurrencyUnit(String code, int name, int symbol, int flag) {
        this.code = code;
        this.name = name;
        this.symbol= symbol;
        this.flag = flag;
    }

    public CurrencyUnit() {}
}
