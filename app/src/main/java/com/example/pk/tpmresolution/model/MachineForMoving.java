package com.example.pk.tpmresolution.model;

/**
 * Created by Julian on 5/11/2017.
 */

public class MachineForMoving {
    String name;
    String MachineId;
    String FrFactory;
    String FrLine;
    String FrWh;
    String ToFactory;
    String ToLine;
    String ToWh;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMachineId() {
        return MachineId;
    }

    public void setMachineId(String machineId) {
        MachineId = machineId;
    }

    public String getFrFactory() {
        return FrFactory;
    }

    public void setFrFactory(String frFactory) {
        FrFactory = frFactory;
    }

    public String getFrLine() {
        return FrLine;
    }

    public void setFrLine(String frLine) {
        FrLine = frLine;
    }

    public String getFrWh() {
        return FrWh;
    }

    public void setFrWh(String frWh) {
        FrWh = frWh;
    }

    public String getToFactory() {
        return ToFactory;
    }

    public void setToFactory(String toFactory) {
        ToFactory = toFactory;
    }

    public String getToLine() {
        return ToLine;
    }

    public void setToLine(String toLine) {
        ToLine = toLine;
    }

    public String getToWh() {
        return ToWh;
    }

    public void setToWh(String toWh) {
        ToWh = toWh;
    }
}
