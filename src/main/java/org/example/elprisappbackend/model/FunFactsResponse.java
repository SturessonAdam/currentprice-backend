package org.example.elprisappbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunFactsResponse {
    
    @JsonProperty("evChargeCost")
    private Double evChargeCost;
    
    @JsonProperty("heatPumpDayCost")
    private Double heatPumpDayCost;
    
    @JsonProperty("showerCost")
    private Double showerCost;
    
    @JsonProperty("washerCost")
    private Double washerCost;
    
    @JsonProperty("dryerCost")
    private Double dryerCost;
    
    @JsonProperty("dishwasherCost")
    private Double dishwasherCost;
}