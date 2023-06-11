package com.myou.sample.app.controller.sample01;

import java.io.Serializable;

import javax.validation.constraints.Negative;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotEmpty;

public class Sample01Form implements Serializable{

    @NotEmpty
    private String data1;

    @NotEmpty
    private String data2;

    @NotEmpty(message="{check.001}")
    private String data3;

    @Negative
    private Integer number1;

    @NegativeOrZero
    private Integer number2;

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public Integer getNumber1() {
        return number1;
    }

    public void setNumber1(Integer number1) {
        this.number1 = number1;
    }

    public Integer getNumber2() {
        return number2;
    }

    public void setNumber2(Integer number2) {
        this.number2 = number2;
    }
}
