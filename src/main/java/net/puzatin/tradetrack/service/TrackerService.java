package net.puzatin.tradetrack.service;

import net.puzatin.tradetrack.model.Tracker;

import java.util.List;

public interface TrackerService {

    List<Tracker> getAll();

    void add(Tracker track);

}
