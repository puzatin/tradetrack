package net.puzatin.tradetrack.service;


import net.puzatin.tradetrack.model.Snapshot;
import net.puzatin.tradetrack.model.Tracker;

import java.util.List;

public interface SnapshotService {

    List<Snapshot> findByPubKey(String pubKey);

    Long getLastTimestamp(String pubKey);

    Double getFirstBalanceInUSDT(String pubKey);

    Double getFirstBalanceInBTC(String pubKey);


    void add(Snapshot snapshot);

    void firstSnapshot(Tracker tracker);

}
