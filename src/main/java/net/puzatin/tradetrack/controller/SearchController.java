package net.puzatin.tradetrack.controller;


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


@Controller
public class SearchController {

    @Autowired
    private TrackerService trackerService;


    @GetMapping("/search")
    public String search(@RequestParam String tracker, Model model){
        model.addAttribute("tracker", trackerService.findByPubKey(tracker));
        return "search";
    }

}
