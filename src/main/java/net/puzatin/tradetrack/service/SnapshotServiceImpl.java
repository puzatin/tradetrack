package net.puzatin.tradetrack.service;

import net.puzatin.tradetrack.model.Snapshot;
import net.puzatin.tradetrack.repository.SnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.NumberFormat;


@Service
@Transactional
public class SnapshotServiceImpl implements SnapshotService {

    @Autowired
    SnapshotRepository snapshotRepository;

    @Override
    public void add(Snapshot snapshot) {
        snapshotRepository.save(snapshot);
    }
}
