package net.puzatin.tradetrack.service;


import net.puzatin.tradetrack.model.Snapshot;

import java.util.List;

public interface SnapshotService {

    List<Snapshot> findByPubKey(String pubKey);

    Long getLastTimestamp(String pubKey);


    void add(Snapshot snapshot);

}
