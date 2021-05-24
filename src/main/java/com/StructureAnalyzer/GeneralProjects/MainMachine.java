package com.StructureAnalyzer.GeneralProjects;

import java.util.Objects;

public class MainMachine {

    private String leverancier;
    private String ordernummer;


    public MainMachine(String leverancier, String ordernummer) {
        this.leverancier = leverancier;
        this.ordernummer = ordernummer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MainMachine that = (MainMachine) o;
        return Objects.equals(leverancier, that.leverancier) &&
                Objects.equals(ordernummer, that.ordernummer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leverancier, ordernummer);
    }

    public String getLeverancier() {
        return leverancier;
    }

    public void setLeverancier(String leverancier) {
        this.leverancier = leverancier;
    }

    public String getOrdernummer() {
        return ordernummer;
    }

    public void setOrdernummer(String ordernummer) {
        this.ordernummer = ordernummer;
    }

    @Override
    public String toString() {
        return "MainMachine{" +
                "leverancier='" + leverancier + '\'' +
                ", ordernummer='" + ordernummer + '\'' +
                '}';
    }
}
