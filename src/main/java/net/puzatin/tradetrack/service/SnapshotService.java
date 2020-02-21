package net.puzatin.tradetrack.service;


import net.puzatin.tradetrack.model.ChartData;
import net.puzatin.tradetrack.model.Snapshot;
import net.puzatin.tradetrack.model.Tracker;

import java.util.List;

public interface SnapshotService {

    List<Snapshot> findByPubKey(String pubKey);

    Long getLastTimestamp(String pubKey);

    Double getFirstBalanceInUSDT(String pubKey);

    Double getFirstBalanceInBTC(String pubKey);

    Double getSumDeltaDepInUSDT(String pubKey);

    Double getSumDeltaDepInBTC(String pubKey);

    void add(Snapshot snapshot);

    void firstSnapshot(Tracker tracker);

    ChartData fillChartData(Tracker tracker);

}
