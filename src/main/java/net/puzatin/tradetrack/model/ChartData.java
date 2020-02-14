package net.puzatin.tradetrack.model;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChartData {

    private String name;

    private List<Long> date;

    private List<Double> profitInBTC;

    private List<Double> profitInUSDT;

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
