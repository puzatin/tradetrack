package net.puzatin.tradetrack.service;

import net.puzatin.tradetrack.model.Tracker;
import net.puzatin.tradetrack.repository.TrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TrackerServiceImpl implements TrackerService {

    @Autowired
    private TrackerRepository trackerRepository;

    @Override
    public Tracker findByName(String name) {
        return trackerRepository.findByName(name);
    }

    @Override
    public List<Tracker> getAll() {
        return trackerRepository.findAll();
    }

    @Override
    public void add(Tracker tracker) {
        trackerRepository.save(tracker);
    }
}
