package net.puzatin.tradetrack.controller;


import net.puzatin.tradetrack.model.ChartData;
import net.puzatin.tradetrack.model.Snapshot;
import net.puzatin.tradetrack.model.Tracker;
import net.puzatin.tradetrack.service.SnapshotService;
import net.puzatin.tradetrack.service.TrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;


@Controller
public class SearchController {

    @Autowired
    private TrackerService trackerService;

    @Autowired
    private SnapshotService snapshotService;


    @GetMapping("/search")
    public String search(@RequestParam String trackerPubKey, Model model){

        Tracker tracker = trackerService.findByPubKey(trackerPubKey);

        model.addAttribute("snapshot", snapshotService.fillChartData(tracker));

        return "search";
    }

}
