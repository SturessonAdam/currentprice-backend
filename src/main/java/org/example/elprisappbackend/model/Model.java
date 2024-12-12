package org.example.elprisappbackend.model;

import java.time.LocalDateTime;

public class Model {

    private double sekPerKWh;
    private double eurPerKWh;
    private double exr;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;

    public double getSekPerKWh() {
        return sekPerKWh;
    }

    public void setSekPerKWh(double sekPerKWh) {
        this.sekPerKWh = sekPerKWh;
    }

    public double getEurPerKWh() {
        return eurPerKWh;
    }

    public void setEurPerKWh(double eurPerKWh) {
        this.eurPerKWh = eurPerKWh;
    }

    public double getExr() {
        return exr;
    }

    public void setExr(double exr) {
        this.exr = exr;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

}
