package net.puzatin.tradetrack.model;



import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;


@Entity
@Table(name = "snapshots")
public class Snapshot {

    @Id @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "pubKey")
    private Tracker tracker;

    private double balanceInBTC;

    private double balanceInUSDT;

    private double deltaDepositInBTC;

    private double deltaDepositInUSDT;

    private double profitInBTC;

    private double profitInUSDT;

    private long timestamp;

    public double getProfitInBTC() {
        return profitInBTC;
    }

    public void setProfitInBTC(double profitInBTC) {
        this.profitInBTC = profitInBTC;
    }

    public double getProfitInUSDT() {
        return profitInUSDT;
    }

    public void setProfitInUSDT(double profitInUSDT) {
        this.profitInUSDT = profitInUSDT;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getDeltaDepositInBTC() {
        return deltaDepositInBTC;
    }

    public void setDeltaDepositInBTC(double deltaDepositInBTC) {
        this.deltaDepositInBTC = deltaDepositInBTC;
    }

    public double getDeltaDepositInUSDT() {
        return deltaDepositInUSDT;
    }

    public void setDeltaDepositInUSDT(double deltaDepositInUSDT) {
        this.deltaDepositInUSDT = deltaDepositInUSDT;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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


}
