package se.elektromin.buzztimer.api;

import com.google.common.primitives.Doubles;
import lombok.Data;
import lombok.NonNull;
import se.elektromin.buzztimer.endpoint.NearbyStops;
import se.elektromin.buzztimer.endpoint.RealtimeDepartures;
import se.elektromin.buzztimer.endpoint.StopLocation;

import java.util.ArrayList;
import java.util.List;

@Data
public class StationReponse {
    @NonNull
    private String id, name;
    private Double latitude, longitude;
    private Integer distance;
    @NonNull
    private List<Departure> departures = new ArrayList<>();

    public static StationReponse rampoff(@NonNull final StopLocation sl, final List<RealtimeDepartures.ResponseData.Bus> buses) {
        StationReponse resp = new StationReponse(sl.getSiteId(), sl.getName());
        resp.setLatitude(sl.getLat() != null ? Doubles.tryParse(sl.getLat()) : null);
        resp.setLongitude(sl.getLon() != null ? Doubles.tryParse(sl.getLon()) : null);
        resp.setDistance(sl.getDistance());

        if (buses != null) {
            for (RealtimeDepartures.ResponseData.Bus bus : buses) {
                resp.departures.add(Departure.rampoff(bus));
            }
        }
        return resp;
    }

    public static StationReponse rampoff(@NonNull final NearbyStops nearbyStops) {
        if (nearbyStops.getLocationList().getStopLocation().size() > 0) {
            return rampoff(nearbyStops.getLocationList().getStopLocation().get(0), null);
        }
        return null;
    }
}
