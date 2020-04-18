package com.covid19.tracker.repository;

import com.covid19.tracker.config.EndpointConfig;
import com.covid19.tracker.model.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.*;

@Service
public class Covid19DataService_Country {

    @Autowired
    private EndpointConfig endpointConfig;

    private List<LocationStats> allStats = new ArrayList<>();
    private List<LocationStats> sortedStats =new ArrayList<>();
    public List<LocationStats> getAllStats() {
        return allStats;
    }
    public List<LocationStats> getSortedStats(){
        return sortedStats;
    }

    public void getConfirmedCases() throws URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(new URI("")).build();
    }
//Ji0inf8/47

    @PostConstruct
    @Scheduled(cron = "0 0 */1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointConfig.getConfirmedCases()))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        Iterable<CSVRecord> records2 = records;

        HashMap<String, Integer> map = new HashMap<>();
        HashMap<String, Integer> mapPrev = new HashMap<>();
        Map<Integer,String> valToKey = new TreeMap<>(Collections.reverseOrder());

        for (CSVRecord record : records2) {
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            locationStat.setLatestTotalCases(latestCases);
            locationStat.setDiffFromPrevDay(latestCases - prevDayCases);
            newStats.add(locationStat);
            String str = record.get("Country/Region").toString();
            if(map.containsKey(str)){
                int a = map.get(str);
                a+=latestCases;
                int b = mapPrev.get(str);
                b+=prevDayCases;
                map.replace(str,a);
                mapPrev.replace(str,b);
            }
            else{
                map.put(str,latestCases);
                mapPrev.put(str,prevDayCases);
            }
        }
        this.allStats = newStats;


        Iterator itr = map.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry me = (Map.Entry)itr.next();
            valToKey.put((int)me.getValue(),me.getKey().toString());
        }

        List<LocationStats> sStats = new ArrayList<>();

        Iterator i = valToKey.entrySet().iterator();
        while(i.hasNext()){
            Map.Entry entry = (Map.Entry)i.next();
            LocationStats ls = new LocationStats();
            ls.setState("-");
            ls.setCountry(entry.getValue().toString());
            ls.setLatestTotalCases((int)entry.getKey());
            ls.setDiffFromPrevDay((int)entry.getKey()-(int)mapPrev.get(entry.getValue().toString()));
            sStats.add(ls);
        }

        System.out.println(map);
        this.sortedStats = sStats;
    }
}