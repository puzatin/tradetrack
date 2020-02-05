package net.puzatin.tradetrack.controller;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import net.puzatin.tradetrack.model.Tracker;
import net.puzatin.tradetrack.service.TrackerService;
import net.puzatin.tradetrack.util.TrackerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private TrackerService trackerService;

    @Autowired
    private TrackerValidator trackerValidator;


    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("tracker", new Tracker());
        model.addAttribute("trackers", trackerService.getAllPublic());

        return "home";
    }



    @PostMapping("/")
    public String addTracker(@ModelAttribute Tracker tracker, BindingResult result){
        trackerValidator.validate(tracker, result);
        if(result.hasErrors()) {
            return "home";
        }
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(tracker.getPubKey(),tracker.getSecKey());
        BinanceApiRestClient client = factory.newRestClient();

        // Test connectivity
        client.ping();


        trackerService.add(tracker);
            return "redirect:/";
    }

}
