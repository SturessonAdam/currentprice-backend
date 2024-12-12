package org.example.elprisappbackend.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class Model {

    private double sekPerKWh;
    private double eurPerKWh;
    private double exr;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;

}
