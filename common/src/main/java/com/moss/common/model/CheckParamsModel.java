package com.moss.common.model;

public class CheckParamsModel {

    private String name;
    private int minLength;
    private int maxLength;
    private String reg;

    public CheckParamsModel(String name, int minLength, int maxLength, String reg) {
        this.name = name;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.reg = reg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }
}
