package se.elektromin.buzztimer.endpoint;

import lombok.Data;

import java.util.List;

@Data
public class LocationList {
    private List<StopLocation> StopLocation;
}
