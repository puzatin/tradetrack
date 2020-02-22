package net.puzatin.tradetrack.controller;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import net.puzatin.tradetrack.model.ChartData;
import net.puzatin.tradetrack.model.Snapshot;
import net.puzatin.tradetrack.model.Tracker;
import net.puzatin.tradetrack.service.SnapshotService;
import net.puzatin.tradetrack.service.TrackerService;
import net.puzatin.tradetrack.util.TrackerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class HomeController {

    @Autowired
    private TrackerService trackerService;

    @Autowired
    private SnapshotService snapshotService;

    @Autowired
    private TrackerValidator trackerValidator;





    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("tracker", new Tracker());
        List<Tracker> trackerList = trackerService.getAllPublicAndSnapshotMore24();
        List<ChartData> listChartData = new ArrayList<>();
        trackerList.forEach(tracker -> {
            listChartData.add(snapshotService.fillChartData(tracker));
        });
        model.addAttribute("list", listChartData);

        return "home";
    }

    @RequestMapping(path = {"/tracker/{pubKey}/edit"})
    public String editTracker(Model model, @PathVariable("pubKey") Optional<String> pubKey){

        if (pubKey.isPresent()) {
            Tracker tracker = trackerService.findByPubKey(pubKey.get());
            model.addAttribute("tracker", tracker);
        }
        return "edit-tracker";
    }


    @GetMapping(value = {"/tracker/{name}"})
    public String findByName(@PathVariable("name") String name, Model model){
        Tracker tracker;
        if (name.length() == 64) {
          tracker =  trackerService.findByPubKey(name);
          model.addAttribute("tracker", tracker);
        } else {
            tracker = trackerService.findByisPublicTrueAndName(name);
        }

        if(tracker != null){
            model.addAttribute("snapshot", snapshotService.fillChartData(tracker));
        }
        return "search";
    }


    @PostMapping("/")
    public String addTracker(@Valid @ModelAttribute Tracker tracker, BindingResult result){

        trackerValidator.validate(tracker, result);

        if(result.hasErrors()) {
            return "home";
        }

        trackerService.add(tracker);
        snapshotService.firstSnapshot(tracker);
            return "redirect:/";
    }

    @PostMapping("/update")
    public String updateTracker(@Valid @ModelAttribute Tracker tracker, BindingResult result){

        String oldName = trackerService.findByPubKey(tracker.getPubKey()).getName();

        if (!oldName.equals(tracker.getName())) {
            trackerValidator.nameValidate(tracker, result);
        }

        if(result.hasErrors()) {
            return "edit-tracker";
        }
        trackerService.update(tracker);
        return "redirect:/tracker/" + tracker.getPubKey();

    }

    @GetMapping(path = "/tracker/{pubKey}/delete")
    public String deleteTracker(@PathVariable("pubKey")  String pubKey){
        trackerService.delete(pubKey);
        return "redirect:/";
    }

}
