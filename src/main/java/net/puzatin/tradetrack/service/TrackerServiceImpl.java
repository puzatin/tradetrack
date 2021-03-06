package net.puzatin.tradetrack.service;

import net.puzatin.tradetrack.model.Tracker;
import net.puzatin.tradetrack.repository.TrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Tracker findByisPublicTrueAndName(String name) {
        return trackerRepository.findByisPublicTrueAndName(name);
    }

    @Override
    public List<Tracker> getAllPublic() {
        return trackerRepository.findByisPublicTrue();
    }

    @Override
    public List<Tracker> getAllPublicAndSnapshotMore24() {
        return trackerRepository.getAllisPublicAndSnapshotMore24();
    }

    @Override
    public List<Tracker> getAllValid() {
        return trackerRepository.findByisValidTrue();
    }

    @Override
    public List<Tracker> getAll() {
        return trackerRepository.findAll();
    }

    @Override
    public void add(Tracker tracker) {
        trackerRepository.save(tracker);
    }

    @Override
    public void update(Tracker tracker) {
       Optional<Tracker> track = trackerRepository.findById(tracker.getPubKey());
        if(track.isPresent()){
            Tracker updTrack = track.get();
            updTrack.setName(tracker.getName());
            updTrack.setDescription(tracker.getDescription());
            updTrack.setPublic(tracker.isPublic());

            trackerRepository.save(updTrack);
        } else {
            trackerRepository.save(tracker);
        }

    }

    @Override
    public void delete(String pubKey) {
        Optional<Tracker> tracker = trackerRepository.findById(pubKey);

        if(tracker.isPresent())
            trackerRepository.deleteById(pubKey);
    }


    @Override
    public void setInvalid(Tracker tracker) {
        tracker.setValid(false);
        trackerRepository.save(tracker);
    }

    @Override
    public void setValid(Tracker tracker) {
        tracker.setValid(true);
        trackerRepository.save(tracker);
    }

    @Override
    public Tracker findByPubKey(String pubKey) {
        return trackerRepository.findByPubKey(pubKey);
    }

    @Override
    public Tracker findBySecKey(String secKey) {
        return trackerRepository.findBySecKey(secKey);
    }
}
