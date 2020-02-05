package net.puzatin.tradetrack.model;


import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;

@Entity
@Table(name = "snapshots")
public class Snapshot {

    @Id @GeneratedValue
    private long Id;

    @ManyToOne
    @JoinColumn(name = "pubKey")
    private Tracker tracker;

    private double balanceInBTC;

    private double balanceInUSDT;

    private String date;

    private long timestamp;


    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public double getBalanceInBTC() {
        return balanceInBTC;
    }

    public void setBalanceInBTC(double balanceInBTC) {
        this.balanceInBTC = balanceInBTC;
    }

    public double getBalanceInUSDT() {
        return balanceInUSDT;
    }

    public void setBalanceInUSDT(double balanceInUSDT) {
        this.balanceInUSDT = balanceInUSDT;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


}
