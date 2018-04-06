package se.elektromin.buzztimer.service;

import lombok.Setter;
import se.elektromin.buzztimer.endpoint.NearbyStops;
import se.elektromin.buzztimer.endpoint.RealtimeDepartures;
import se.elektromin.buzztimer.endpoint.SlApiEndpoint;
import se.elektromin.buzztimer.endpoint.StopLocation;

import java.util.*;

public class DepartureService {
    private static final int DEFAULT_RADIUS = 2000;
    private static final List<String> FAVORITE_LINES = Arrays.asList("401", "801", "814", "816", "821");

    @Setter
    private SlApiEndpoint slApiEndpoint;

    public NearbyStops getNearbyStops(final double latitude, final double longitude) {
        return slApiEndpoint.getNearbyStops(latitude, longitude, DEFAULT_RADIUS);
    }

    public RealtimeDepartures getRealtimeDepartures(final String stationId) {
        return slApiEndpoint.getRealtimeDepartures(stationId);
    }

    public Map<StopLocation, List<RealtimeDepartures.ResponseData.Bus>> getDepartures(final double latitude, final double longitude) {
        final Map<StopLocation, List<RealtimeDepartures.ResponseData.Bus>> result = new TreeMap<>();
        final NearbyStops stops = getNearbyStops(latitude, longitude);
        if (stops.getLocationList() != null) {
            for (StopLocation stopLocation : stops.getLocationList().getStopLocation()) {
                List<RealtimeDepartures.ResponseData.Bus> buses = new ArrayList<>();
                final RealtimeDepartures departures = getRealtimeDepartures(stopLocation.getSiteId());

                if (departures.getResponseData() != null && departures.getResponseData().getBuses() != null) {
                    for (RealtimeDepartures.ResponseData.Bus bus : departures.getResponseData().getBuses()) {
                        if (FAVORITE_LINES.contains(bus.getLineNumber())) {
                            buses.add(bus);
                        }
                    }
                }

                if (!buses.isEmpty()) {
                    result.put(stopLocation, buses);
                }
            }
        }
        return result;
    }
}
