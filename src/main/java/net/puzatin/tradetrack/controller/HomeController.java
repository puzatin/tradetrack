package net.puzatin.tradetrack.controller;


import net.puzatin.tradetrack.model.ChartData;
import net.puzatin.tradetrack.model.Tracker;
import net.puzatin.tradetrack.service.SnapshotService;
import net.puzatin.tradetrack.service.TrackerService;
import net.puzatin.tradetrack.util.ErrorMessage;
import net.puzatin.tradetrack.util.TrackerValidator;
import net.puzatin.tradetrack.util.ValidationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


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
        Map<Tracker, Double> map = new HashMap<>();
        List<ChartData> listChartData = new ArrayList<>();
        trackerList.forEach(tracker -> {
            double lastBalanceUSDT = snapshotService.getLastSnapshot(tracker).getBalanceInUSDT();
            Double firstBalanceUSDT = snapshotService.getFirstBalanceInUSDT(tracker.getPubKey());
            Double sumDeltaUSDT = snapshotService.getSumDeltaDepInUSDT(tracker.getPubKey());
            Double profitForTop = lastBalanceUSDT / (firstBalanceUSDT + sumDeltaUSDT) * 100 - 100;
            map.put(tracker, profitForTop);

        });

        map.entrySet().stream()
                .sorted(Map.Entry.<Tracker, Double>comparingByValue().reversed()).limit(10)
                .forEach(entryMap -> {
                    listChartData.add(snapshotService.fillChartData(entryMap.getKey()));
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
            model.addAttribute("onlyFutures", tracker.isOnlyFutures());
            model.addAttribute("snapshot", snapshotService.fillChartData(tracker));
            return "search";
        } else return "error";

    }


    @PostMapping("/")
    @Transactional
    public @ResponseBody ValidationResponse addTracker(@Valid @ModelAttribute Tracker tracker, BindingResult result){

        ValidationResponse res = new ValidationResponse();
        trackerValidator.validate(tracker, result);

        if(result.hasErrors()) {
            res.setStatus("FAIL");
            List<FieldError> allErrors = result.getFieldErrors();

            final List<ErrorMessage> errorMessages = new ArrayList<>();
            for (FieldError objectError : allErrors) {
                errorMessages.add(new ErrorMessage(objectError.getField(), objectError.getDefaultMessage()));
            }
            res.setErrorMessageList(errorMessages);
        } else {
            trackerService.add(tracker);
            snapshotService.firstSnapshot(tracker);
        }

            return res;
    }

    @PostMapping("/update")
    public @ResponseBody ValidationResponse update(@Valid @ModelAttribute Tracker tracker, BindingResult result){


        ValidationResponse res = new ValidationResponse();
        String oldName = trackerService.findByPubKey(tracker.getPubKey()).getName();

        if (!oldName.equals(tracker.getName())) {
            trackerValidator.nameValidate(tracker, result);
        }

        if(result.hasErrors()) {
            res.setStatus("FAIL");
            List<FieldError> allErrors = result.getFieldErrors();
            final List<ErrorMessage> errorMessages = new ArrayList<>();
            for (FieldError objectError : allErrors) {
                errorMessages.add(new ErrorMessage(objectError.getField(), objectError.getDefaultMessage()));
            }
            res.setErrorMessageList(errorMessages);
        } else {
            trackerService.update(tracker);
        }

        return res;

    }

    @GetMapping(path = "/tracker/{pubKey}/delete")
    public String deleteTracker(@PathVariable("pubKey")  String pubKey){
        trackerService.delete(pubKey);
        return "redirect:/";
    }

}
