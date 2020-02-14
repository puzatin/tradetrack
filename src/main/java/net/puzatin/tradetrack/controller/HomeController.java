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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;


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
            List<Snapshot> snapshotList = snapshotService.findByPubKey(tracker.getPubKey());
            ChartData chartData = new ChartData();
            chartData.setName(tracker.getName());
            List<Double> balanceBTC = new ArrayList<>();
            List<Double> balanceUSDT = new ArrayList<>();
            List<Long> date = new ArrayList<>();
            snapshotList.forEach(snapshot -> {
                balanceBTC.add(snapshot.getProfitInBTC());
                balanceUSDT.add(snapshot.getProfitInUSDT());
                date.add(snapshot.getTimestamp());
            });
            chartData.setProfitInBTC(balanceBTC);
            chartData.setProfitInUSDT(balanceUSDT);
            chartData.setDate(date);
            listChartData.add(chartData);
        });
        model.addAttribute("list", listChartData);

        return "home";
    }



    @PostMapping("/")
    public String addTracker(@ModelAttribute Tracker tracker, BindingResult result){
        trackerValidator.validate(tracker, result);
        if(result.hasErrors()) {
            return "home";
        }
        trackerService.add(tracker);
        snapshotService.firstSnapshot(tracker);
            return "redirect:/";
    }

}
