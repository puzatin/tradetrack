package net.puzatin.tradetrack.service;

import net.puzatin.tradetrack.model.Tracker;
import net.puzatin.tradetrack.repository.TrackerRepository;

import java.util.List;

public interface TrackerService {

    Tracker findByName(String name);

    List<Tracker> getAllPublic();

    List<Tracker> getAllPublicAndSnapshotMore24();

    List<Tracker> getAllValid();

    List<Tracker> getAll();

    void add(Tracker track);


    Tracker findByPubKey(String pubKey);

    Tracker findBySecKey(String secKey);

}
