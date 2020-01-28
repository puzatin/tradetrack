package net.puzatin.tradetrack.controller;

import net.puzatin.tradetrack.model.Tracker;
import net.puzatin.tradetrack.service.TrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeContoller {

    @Autowired
    private TrackerService trackerService;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("tracker", new Tracker());
        return "home";
    }

    @PostMapping("/")
    public String addTracker(@ModelAttribute Tracker tracker, BindingResult result){
        if(result.hasErrors()) {
            System.out.println("Ошибка при добавлении трекера");
            return "home";
        }
            trackerService.add(tracker);
        System.out.println("Попытка добавить трекер");
            return "home";
    }

}
