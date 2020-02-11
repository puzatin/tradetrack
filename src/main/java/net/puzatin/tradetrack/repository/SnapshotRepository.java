package net.puzatin.tradetrack.repository;

import net.puzatin.tradetrack.model.Snapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnapshotRepository extends JpaRepository<Snapshot, Long> {

    @Query(value = "SELECT * FROM snapshots WHERE pub_key=?", nativeQuery = true)
    List<Snapshot> findBypubKey(String pubKey);

    @Query(value = "SELECT timestamp from snapshots WHERE pub_key=? ORDER BY timestamp DESC LIMIT 1", nativeQuery = true)
    Long getLastTimestamp(String pubKey);

}
