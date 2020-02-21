package net.puzatin.tradetrack.repository;

import net.puzatin.tradetrack.model.Tracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackerRepository extends JpaRepository<Tracker, String> {

    Tracker findByName(String name);

    Tracker findByisPublicTrueAndName(String name);

    List<Tracker> findByisValidTrue();

    List<Tracker> findByisPublicTrue();

    Tracker findByPubKey(String pubKey);

    Tracker findBySecKey(String secKey);

    @Query(value = "SELECT * FROM trackers WHERE pub_key IN" +
            "                             (SELECT pub_key from (" +
            "                                 SELECT COUNT(*) as count, pub_key" +
            "                                      FROM  snapshots" +
            "                                      WHERE pub_key=pub_key" +
            "                                      GROUP BY pub_key" +
            "                                      HAVING count > 24) as snapshotsMore24Rows" +
            "                                 ) AND is_public = TRUE", nativeQuery = true)
    List<Tracker> getAllisPublicAndSnapshotMore24();


}
