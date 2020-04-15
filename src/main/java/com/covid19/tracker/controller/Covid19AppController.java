package com.covid19.tracker.controller;

import com.covid19.tracker.model.LocationStats;
import com.covid19.tracker.repository.Covid19DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class Covid19AppController {

    @Autowired
    Covid19DataService covid19DataService;

    @GetMapping("/")
    public String home(Model model) {
        List<LocationStats> allStats = covid19DataService.getAllStats();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        System.out.println("totalReportedCases : "+totalReportedCases);
        System.out.println("totalNewCases : "+totalNewCases);
        return "home";
    }
}