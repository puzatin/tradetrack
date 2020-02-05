package net.puzatin.tradetrack.service;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.constant.Util;
import com.binance.api.client.domain.account.Account;
import com.binance.api.client.domain.account.AssetBalance;
import net.puzatin.tradetrack.model.Snapshot;
import net.puzatin.tradetrack.model.Tracker;
import net.puzatin.tradetrack.repository.TrackerRepository;
import net.puzatin.tradetrack.util.BinanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SchedulerService {

    @Autowired
    private TrackerService trackerService;

    @Autowired
    private SnapshotService snapshotService;

    @Autowired
    private TrackerRepository trackerRepository;

    private static ThreadLocal<NumberFormat> formatBTC =
            ThreadLocal.withInitial(() -> new DecimalFormat("0.00000000"));
    private static ThreadLocal<NumberFormat> formatUSDT =
            ThreadLocal.withInitial(() -> new DecimalFormat("0.00"));


    @Scheduled(cron = "*/10 * * * * *")
    public void recordSnapshot(){


        if(BinanceUtil.ping()) {
            List<Tracker> trackers = trackerService.getAllValid();
            trackers.parallelStream().forEach(tracker -> {
                Snapshot snapshot = new Snapshot();
                System.out.println(tracker.getName());
                String pub = tracker.getPubKey();
                String sec = tracker.getSecKey();


                double balanceInBTC = BinanceUtil.getTotalAccountBalanceInBTC(pub, sec);

                if (balanceInBTC == -1) {
                    tracker.setValid(false);
                    trackerRepository.save(tracker);
                }
                balanceInBTC = Double.parseDouble(formatBTC.get().format(BinanceUtil.getTotalAccountBalanceInBTC(pub, sec)).replace(',', '.'));
                double balanceInUSDT = Double.parseDouble(formatUSDT.get().format(BinanceUtil.getTotalAccountBalanceInUSDT(BinanceUtil.getBTCprice(), balanceInBTC)).replace(',', '.'));

                snapshot.setBalanceInBTC(balanceInBTC);
                snapshot.setBalanceInUSDT(balanceInUSDT);
                snapshot.setTimestamp(Instant.now().getEpochSecond());
                snapshot.setDate(OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                snapshot.setTracker(tracker);
                snapshotService.add(snapshot);

            });

        }
    }



}
