package net.puzatin.tradetrack.repository;

import net.puzatin.tradetrack.model.Snapshot;
import net.puzatin.tradetrack.model.Tracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface SnapshotRepository extends JpaRepository<Snapshot, Long> {

    @Query(value = "SELECT * FROM tracker_snapshots WHERE own_pub_key=?", nativeQuery = true)
    List<Snapshot> findBypubKey(String pubKey);

    @Query(value = "SELECT timestamp from tracker_snapshots WHERE own_pub_key=? ORDER BY timestamp DESC LIMIT 1", nativeQuery = true)
    Long getLastTimestamp(String pubKey);

    @Query(value = "SELECT balance_usdt FROM tracker_snapshots WHERE own_pub_key=? ORDER BY timestamp LIMIT 1", nativeQuery = true)
    Double getFirstBalanceUSDT(String pubKey);

    @Query(value = "SELECT balance_btc FROM tracker_snapshots WHERE own_pub_key=? ORDER BY timestamp LIMIT 1", nativeQuery = true)
    Double getFirstBalanceBTC(String pubKey);

    Snapshot findTopByTrackerOrderByTimestampDesc(Tracker tracker);

    @Query(value = "SELECT SUM(delta_deposit_usdt) FROM tracker_snapshots WHERE own_pub_key=?", nativeQuery = true)
    Double getSumDeltaDepInUSDT(String pubKey);

    @Query(value = "SELECT SUM(delta_deposit_btc) FROM tracker_snapshots WHERE own_pub_key=?", nativeQuery = true)
    Double getSumDeltaDepInBTC(String pubKey);

}
