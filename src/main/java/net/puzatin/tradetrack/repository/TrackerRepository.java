package net.puzatin.tradetrack.repository;

import net.puzatin.tradetrack.model.Tracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackerRepository extends JpaRepository<Tracker, String> {

    Tracker findByName(String name);


}
