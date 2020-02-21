package net.puzatin.tradetrack.service;

import net.puzatin.tradetrack.model.ChartData;
import net.puzatin.tradetrack.model.Snapshot;
import net.puzatin.tradetrack.model.Tracker;
import net.puzatin.tradetrack.repository.SnapshotRepository;
import net.puzatin.tradetrack.repository.TrackerRepository;
import net.puzatin.tradetrack.util.BinanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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

    private static ThreadLocal<NumberFormat> formatBTC =
            ThreadLocal.withInitial(() -> new DecimalFormat("0.00000000"));
    private static ThreadLocal<NumberFormat> formatUSDT =
            ThreadLocal.withInitial(() -> new DecimalFormat("0.00"));


    @Override
    public List<Snapshot> findByPubKey(String pubKey) {
        return snapshotRepository.findBypubKey(pubKey);
    }

    @Override
    public Long getLastTimestamp(String pubKey) {
        return snapshotRepository.getLastTimestamp(pubKey);
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
        Snapshot snapshot = new Snapshot();
        boolean first = true;
        HashMap<String, String> prices = BinanceUtil.getPrices();
        double BTCprice = BinanceUtil.getBTCprice();

        double balanceInBTC = BinanceUtil.getTotalAccountBalanceInBTC(tracker.getPubKey(), tracker.getSecKey(), prices, BTCprice, first);
        System.out.println(tracker.getName());
        if (balanceInBTC != -1) {
            balanceInBTC = Double.parseDouble(formatBTC.get().format(balanceInBTC).replace(',', '.'));
            double balanceInUSDT = Double.parseDouble(formatUSDT.get().format(BinanceUtil.getTotalAccountBalanceInUSDT(BTCprice, balanceInBTC)).replace(',', '.'));

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

    }


//    @Scheduled(cron = "0 */5 * * * *")
    public void recordSnapshot(){

        if(BinanceUtil.ping()) {
            boolean first = false;
            HashMap<String, String> prices = BinanceUtil.getPrices();
            double BTCprice = BinanceUtil.getBTCprice();
            List<Tracker> trackers = trackerService.getAllValid();
            trackers.parallelStream().forEach(tracker -> {
                String pub = tracker.getPubKey();
                String sec = tracker.getSecKey();

                Double firstBalanceInUSDT = getFirstBalanceInUSDT(pub);
                Double firstBalanceInBTC = getFirstBalanceInBTC(pub);


                Long lastTimestamp = getLastTimestamp(pub);
                System.out.println(lastTimestamp);
                double deltaDepositInBTC;
                double deltaDepositInUSDT;
                Snapshot snapshot = new Snapshot();
                System.out.println(tracker.getName());


                deltaDepositInBTC = BinanceUtil.getDeltaDepositInBTC(pub, sec, lastTimestamp, prices);
                deltaDepositInUSDT = BinanceUtil.getDeltaDepositInUSDT(BTCprice, deltaDepositInBTC);



                double balanceInBTC = BinanceUtil.getTotalAccountBalanceInBTC(pub, sec, prices, BTCprice, first);

                if (balanceInBTC != -1) {
                    balanceInBTC = Double.parseDouble(formatBTC.get().format(balanceInBTC).replace(',', '.'));
                    double balanceInUSDT = Double.parseDouble(formatUSDT.get().format(BinanceUtil.getTotalAccountBalanceInUSDT(BTCprice, balanceInBTC)).replace(',', '.'));

                    System.out.println(balanceInUSDT);
                    System.out.println(getSumDeltaDepInUSDT(pub));
                    System.out.println(firstBalanceInUSDT);
                    System.out.println(deltaDepositInUSDT);

                    snapshot.setProfitInUSDT(Double.parseDouble(formatUSDT.get().format((balanceInUSDT - (getSumDeltaDepInUSDT(pub) + deltaDepositInUSDT)) / firstBalanceInUSDT * 100 - 100).replace(',', '.')));
                    snapshot.setProfitInBTC(Double.parseDouble(formatUSDT.get().format((balanceInBTC - (getSumDeltaDepInBTC(pub) + deltaDepositInBTC)) / firstBalanceInBTC * 100 - 100).replace(',', '.')));
                    snapshot.setDeltaDepositInBTC(deltaDepositInBTC);
                    snapshot.setDeltaDepositInUSDT(deltaDepositInUSDT);
                    snapshot.setBalanceInBTC(balanceInBTC);
                    snapshot.setBalanceInUSDT(balanceInUSDT);
                    snapshot.setTimestamp(Instant.now().toEpochMilli());
                    snapshot.setTracker(tracker);
                    add(snapshot);
                } else {
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
        chartData.setDescription(tracker.getDescription());
        chartData.setProfitInBTC(balanceBTC);
        chartData.setProfitInUSDT(balanceUSDT);
        chartData.setDate(date);


        return chartData;
    }


}
