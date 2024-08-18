package com.example.rac.notelist.adress;

public class Commission {
    private String chain;

    private double commission1;


    public Commission(String address, double commission1) {

        this.chain = chain;
        this.commission1 = commission1;

    }

    // Геттеры и сеттеры для каждого параметра, если необходимо
    public String getAddress() {
        return chain;
    }

    public void setAddress(String chain) {
        this.chain = chain;
    }


    public double getCommission1() {
        return commission1;
    }

    public void setCommission1(double commission1) {
        this.commission1 = commission1;
    }

}
