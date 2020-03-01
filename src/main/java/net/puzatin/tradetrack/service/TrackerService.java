package net.puzatin.tradetrack.service;

import net.puzatin.tradetrack.model.Tracker;


import java.util.List;

public interface TrackerService {

    Tracker findByName(String name);

    Tracker findByisPublicTrueAndName(String name);

    List<Tracker> getAllPublic();

    List<Tracker> getAllPublicAndSnapshotMore24();

    List<Tracker> getAllValid();

    List<Tracker> getAll();

    void add(Tracker tracker);

    void update(Tracker tracker);

    void delete(String pubKey);

    void setInvalid(Tracker tracker);

    void setValid(Tracker tracker);


    Tracker findByPubKey(String pubKey);

    Tracker findBySecKey(String secKey);

}
