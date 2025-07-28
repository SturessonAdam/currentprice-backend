package org.example.elprisappbackend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceEntry {
    
    @JsonProperty("SEK_per_kWh")
    private Double sekPerKwh;
    
    @JsonProperty("EUR_per_kWh")
    private Double eurPerKwh;
    
    @JsonProperty("EXR")
    private Double exr;
    
    @JsonProperty("time_start")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime timeStart;
    
    @JsonProperty("time_end")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime timeEnd;
}