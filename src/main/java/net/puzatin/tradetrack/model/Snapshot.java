package net.puzatin.tradetrack.model;



import javax.persistence.*;


@Entity
@Table(name = "tracker_snapshots")
public class Snapshot {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "own_pub_key")
    private Tracker tracker;

    @Column(name = "balance_btc")
    private double balanceInBTC;

    @Column(name = "balance_usdt")
    private double balanceInUSDT;


    @Column(name = "delta_deposit_btc")
    private double deltaDepositInBTC;

    @Column(name = "delta_deposit_usdt")
    private double deltaDepositInUSDT;

    @Column(name = "profit_btc")
    private double profitInBTC;

    @Column(name = "profit_usdt")
    private double profitInUSDT;

    @Column(name = "timestamp")
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
