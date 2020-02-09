package net.puzatin.tradetrack.model;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChartData {

    private String name;

    private List<Long> date;

    private List<Double> balanceBTC;

    private List<Double> balanceUSDT;

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

    public List<Double> getBalanceBTC() {
        return balanceBTC;
    }

    public void setBalanceBTC(List<Double> balanceBTC) {
        this.balanceBTC = balanceBTC;
    }

    public List<Double> getBalanceUSDT() {
        return balanceUSDT;
    }

    public void setBalanceUSDT(List<Double> balanceUSDT) {
        this.balanceUSDT = balanceUSDT;
    }
}
