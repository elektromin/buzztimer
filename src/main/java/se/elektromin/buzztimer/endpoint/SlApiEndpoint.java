package se.elektromin.buzztimer.endpoint;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import se.elektromin.buzztimer.util.DefaultEndpoint;
import se.elektromin.buzztimer.util.EndpointException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Invocation;

@Slf4j
public class SlApiEndpoint extends DefaultEndpoint {
    private static final Object MAX_RESULTS = 5;
    private static final Object TIME_WINDOW = 20;

    @Setter
    private String nearbystopsKey;
    @Setter
    private String realtimeDeparturesKey;

    public NearbyStops getNearbyStops(final double latitude, final double longitude, final int radius) {
        final Invocation invocation = getTarget()
                .path("/api2/nearbystops.json")
                .queryParam("key", nearbystopsKey)
                .queryParam("maxresults", MAX_RESULTS)
                .queryParam("originCoordLat", latitude)
                .queryParam("originCoordLong", longitude)
                .queryParam("radius", radius)
                .request()
                .buildGet();

        final EndpointResponse response = invoke(invocation);

        if (response.getStatusCode() == HttpServletResponse.SC_OK) {
            final NearbyStops nearbyStops = getGsonParser().getGson().fromJson(response.getMessage(), NearbyStops.class);
            LOG.info("Nearby stops: " + nearbyStops);
            return nearbyStops;
        } else {
            throw new EndpointException("Failed getting nearby stops. Error code: " + response.getStatusCode() +
                    ", message: " + response.getMessage());
        }
    }

    public RealtimeDepartures getRealtimeDepartures(final String siteId) {
        final Invocation invocation = getTarget()
                .path("/api2/realtimedeparturesV4.json")
                .queryParam("key", realtimeDeparturesKey)
                .queryParam("siteId", siteId)
                .queryParam("timewindow", TIME_WINDOW)
                .request()
                .buildGet();

        final EndpointResponse response = invoke(invocation);

        if (response.getStatusCode() == HttpServletResponse.SC_OK) {
            final RealtimeDepartures realtimeDepartures = getGsonParser().getGson().fromJson(response.getMessage(), RealtimeDepartures.class);
            if (realtimeDepartures.getStatusCode() == 0) {
                LOG.info("Realtime departures: " + realtimeDepartures);
            } else {
                LOG.warn("Failed getting realtime departures. Status code: " + realtimeDepartures.getStatusCode() +
                        ", message: " + realtimeDepartures.getMessage());
            }
            return realtimeDepartures;
        } else {
            throw new EndpointException("Failed getting realtime departures. Error code: " + response.getStatusCode() +
                    ", message: " + response.getMessage());
        }
    }
}
