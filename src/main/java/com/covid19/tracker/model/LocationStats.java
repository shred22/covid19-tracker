package com.covid19.tracker.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LocationStats {

    private String state;
    private String country;
    private int latestTotalCases;
    private int diffFromPrevDay;

}