package net.puzatin.tradetrack.repository;

import net.puzatin.tradetrack.model.Snapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnapshotRepository extends JpaRepository<Snapshot, Long> {


}
