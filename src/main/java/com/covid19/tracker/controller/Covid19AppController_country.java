package com.covid19.tracker.controller;

import com.covid19.tracker.model.LocationStats;
import com.covid19.tracker.repository.Covid19DataService;
import com.covid19.tracker.repository.Covid19DataService_Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class Covid19AppController_country {

    @Autowired
    Covid19DataService covid19DataService;
    @Autowired
    Covid19DataService_Country covid19DataService_Country;

    @GetMapping("/country")
    public String home(Model model) {
        List<LocationStats> allStats = covid19DataService.getAllStats();
        List<LocationStats> sortedStats = covid19DataService_Country.getSortedStats();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        model.addAttribute("locationStats", sortedStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        System.out.println("totalReportedCases : "+totalReportedCases);
        System.out.println("totalNewCases : "+totalNewCases);
        return "home";
    }
}