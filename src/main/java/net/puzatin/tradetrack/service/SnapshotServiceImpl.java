package net.puzatin.tradetrack.service;

import com.binance.api.client.exception.BinanceApiException;
import net.puzatin.tradetrack.model.ChartData;
import net.puzatin.tradetrack.model.Snapshot;
import net.puzatin.tradetrack.model.Tracker;
import net.puzatin.tradetrack.repository.SnapshotRepository;
import net.puzatin.tradetrack.util.BinanceUtil;
import org.decimal4j.util.DoubleRounder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
@Transactional
public class SnapshotServiceImpl implements SnapshotService {

    @Autowired
    private SnapshotRepository snapshotRepository;

    @Autowired
    private TrackerService trackerService;



    @Override
    public List<Snapshot> findByPubKey(String pubKey) {
        return snapshotRepository.findBypubKey(pubKey);
    }

    @Override
    public Long getLastTimestamp(String pubKey) {
        return snapshotRepository.getLastTimestamp(pubKey);
    }

    @Override
    public Snapshot getLastSnapshot(Tracker tracker) {
        return snapshotRepository.findTopByTrackerOrderByTimestampDesc(tracker);
    }

    @Override
    public Double getFirstBalanceInUSDT(String pubKey) {
        return snapshotRepository.getFirstBalanceUSDT(pubKey);
    }

    @Override
    public Double getFirstBalanceInBTC(String pubKey) {
        return snapshotRepository.getFirstBalanceBTC(pubKey);
    }

    @Override
    public Double getSumDeltaDepInUSDT(String pubKey) {
        return snapshotRepository.getSumDeltaDepInUSDT(pubKey);
    }

    @Override
    public Double getSumDeltaDepInBTC(String pubKey) {
        return snapshotRepository.getSumDeltaDepInBTC(pubKey);
    }

    @Override
    public void add(Snapshot snapshot) {
        snapshotRepository.save(snapshot);
    }

    @Override
    public void firstSnapshot(Tracker tracker) {

        boolean first = true;
        HashMap<String, String> prices = BinanceUtil.getPrices();
        double BTCprice = BinanceUtil.getBTCprice();
        double balanceInBTC;
        double balanceInUSDT;

        if (tracker.isOnlyFutures()) {
           balanceInUSDT = BinanceUtil.getTotalFuturesBalance(tracker.getPubKey(), tracker.getSecKey());
           balanceInBTC = balanceInUSDT / BTCprice;
        } else {
            balanceInBTC = BinanceUtil.getTotalAccountBalanceInBTC(tracker.getPubKey(), tracker.getSecKey(), prices, BTCprice, first);
            balanceInUSDT = DoubleRounder.round(BinanceUtil.getTotalAccountBalanceInUSDT(BTCprice, balanceInBTC), 2);

        }

            balanceInBTC = DoubleRounder.round(balanceInBTC, 8);

            Snapshot snapshot = new Snapshot();
            snapshot.setTracker(tracker);
            snapshot.setTimestamp(Instant.now().toEpochMilli());
            snapshot.setBalanceInBTC(balanceInBTC);
            snapshot.setBalanceInUSDT(balanceInUSDT);
            snapshot.setProfitInUSDT(0);
            snapshot.setProfitInBTC(0);
            snapshot.setDeltaDepositInBTC(0);
            snapshot.setDeltaDepositInUSDT(0);
            trackerService.setValid(tracker);
            add(snapshot);


    }


    @Scheduled(cron = "0 0 * * * *")
    public void recordSnapshot(){

        if(BinanceUtil.ping()) {
            boolean first = false;
            HashMap<String, String> prices = BinanceUtil.getPrices();
            double BTCprice = BinanceUtil.getBTCprice();
            System.out.println(BTCprice);
            List<Tracker> trackers = trackerService.getAllValid();

            trackers.parallelStream().forEach(tracker -> {

                try {
                    String pub = tracker.getPubKey();
                    String sec = tracker.getSecKey();
                    BinanceUtil.checkValidAPI(pub, sec);

                    double deltaDepositInBTC;
                    double deltaDepositInUSDT;
                    double balanceInBTC;
                    double balanceInUSDT;
                    double profitInUSDT;
                    double profitInBTC;
                    Double firstBalanceInUSDT = getFirstBalanceInUSDT(pub);
                    Double firstBalanceInBTC = getFirstBalanceInBTC(pub);
                    Long lastTimestamp = getLastTimestamp(pub);

                    if (tracker.isOnlyFutures()) {
                        deltaDepositInUSDT = BinanceUtil.getDeltaDepositFuturesInUSDT(pub, sec, lastTimestamp);
                        deltaDepositInBTC = deltaDepositInUSDT / BTCprice;
                        balanceInUSDT = BinanceUtil.getTotalFuturesBalance(pub, sec);
                        balanceInBTC = balanceInUSDT / BTCprice;
                    } else {
                        deltaDepositInBTC = DoubleRounder.round(BinanceUtil.getDeltaDepositInBTC(pub, sec, lastTimestamp, prices), 8);
                        deltaDepositInUSDT = DoubleRounder.round(BinanceUtil.getDeltaDepositInUSDT(BTCprice, deltaDepositInBTC), 2);
                        balanceInBTC = BinanceUtil.getTotalAccountBalanceInBTC(pub, sec, prices, BTCprice, first);
                        balanceInUSDT = DoubleRounder.round(BinanceUtil.getTotalAccountBalanceInUSDT(BTCprice, balanceInBTC), 2);
                    }

                    profitInBTC = DoubleRounder.round(balanceInBTC / ((getSumDeltaDepInBTC(pub) + deltaDepositInBTC) + firstBalanceInBTC) * 100 - 100, 2);
                    profitInUSDT = DoubleRounder.round(balanceInUSDT / ((getSumDeltaDepInUSDT(pub) + deltaDepositInUSDT) + firstBalanceInUSDT) * 100 - 100, 2);
                    balanceInBTC = DoubleRounder.round(balanceInBTC, 8);


                    Snapshot snapshot = new Snapshot();
                    snapshot.setProfitInUSDT(profitInUSDT);
                    snapshot.setProfitInBTC(profitInBTC);
                    snapshot.setDeltaDepositInBTC(deltaDepositInBTC);
                    snapshot.setDeltaDepositInUSDT(deltaDepositInUSDT);
                    snapshot.setBalanceInBTC(balanceInBTC);
                    snapshot.setBalanceInUSDT(balanceInUSDT);
                    snapshot.setTimestamp(Instant.now().toEpochMilli());
                    snapshot.setTracker(tracker);
                    add(snapshot);
                } catch (BinanceApiException e) {
                    trackerService.setInvalid(tracker);
                }

            });

        }
    }

    public ChartData fillChartData(Tracker tracker){

        List<Snapshot> snapshotList = findByPubKey(tracker.getPubKey());
        ChartData chartData = new ChartData();
        chartData.setName(tracker.getName());
        List<Double> balanceBTC = new ArrayList<>();
        List<Double> balanceUSDT = new ArrayList<>();
        List<Long> date = new ArrayList<>();
        snapshotList.forEach(snapshot -> {
            balanceBTC.add(snapshot.getProfitInBTC());
            balanceUSDT.add(snapshot.getProfitInUSDT());
            date.add(snapshot.getTimestamp());
        });
        chartData.setOnlyFutures(tracker.isOnlyFutures());
        chartData.setDescription(tracker.getDescription());
        chartData.setProfitInBTC(balanceBTC);
        chartData.setProfitInUSDT(balanceUSDT);
        chartData.setDate(date);


        return chartData;
    }


}
