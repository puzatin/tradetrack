package net.puzatin.tradetrack.model;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChartData {

    private String name;

    private String description;

    private List<Long> date;

    private List<Double> profitInBTC;

    private List<Double> profitInUSDT;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getDate() {
        return date;
    }

    public void setDate(List<Long> date) {
        this.date = date;
    }

    public List<Double> getProfitInBTC() {
        return profitInBTC;
    }

    public void setProfitInBTC(List<Double> profitInBTC) {
        this.profitInBTC = profitInBTC;
    }

    public List<Double> getProfitInUSDT() {
        return profitInUSDT;
    }

    public void setProfitInUSDT(List<Double> profitInUSDT) {
        this.profitInUSDT = profitInUSDT;
    }


}
