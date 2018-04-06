package se.elektromin.buzztimer.api;

import lombok.Data;
import lombok.NonNull;
import se.elektromin.buzztimer.endpoint.RealtimeDepartures;
import se.elektromin.buzztimer.endpoint.StopLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DeparturesResponse {
    private static final int MAX_STATIONS = 5;

    @NonNull
    private List<StationReponse> stations = new ArrayList<>();

    public static DeparturesResponse rampoff(final Map<StopLocation, List<RealtimeDepartures.ResponseData.Bus>> departures) {
        final DeparturesResponse response = new DeparturesResponse();

        List<StopLocation> stopLocations = new ArrayList<>(departures.keySet());
        for (int i = 0; i < MAX_STATIONS && i < stopLocations.size(); i++) {
            response.stations.add(StationReponse.rampoff(stopLocations.get(i), departures.get(stopLocations.get(0))));
        }

        return response;
    }

    public static DeparturesResponse rampoff(final String stationId, final RealtimeDepartures realtimeDepartures) {
        final StopLocation sl = new StopLocation();
        sl.setId("00000" + stationId);
        sl.setName(stationId);
        final Map<StopLocation, List<RealtimeDepartures.ResponseData.Bus>> map = new HashMap<>();
        map.put(sl, realtimeDepartures.getResponseData().getBuses());
        return rampoff(map);
    }
}
