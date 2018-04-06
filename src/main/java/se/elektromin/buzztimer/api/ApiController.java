package se.elektromin.buzztimer.api;

import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.elektromin.buzztimer.service.DepartureService;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ApiController {
    @Setter
    private DepartureService departureService;

    @RequestMapping("api/station/closest")
    public StationReponse getClosestStation(@RequestParam(value = "latitude") final double latitude,
                                            @RequestParam(value = "longitude") final double longitude,
                                            final HttpServletResponse response) {
        final StationReponse resp = StationReponse.rampoff(departureService.getNearbyStops(latitude, longitude));
        if (resp == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return resp;
    }

    @RequestMapping("api/departures")
    public DeparturesResponse getDepartures(@RequestParam(value = "stationId") final String stationId,
                                            final HttpServletResponse response) {
        final DeparturesResponse resp = DeparturesResponse.rampoff(stationId, departureService.getRealtimeDepartures(stationId));
        if (resp == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return resp;
    }

    @RequestMapping("api/departures/location")
    public DeparturesResponse getDeparturesForLocation(@RequestParam(value = "latitude") final double latitude,
                                                       @RequestParam(value = "longitude") final double longitude,
                                                       final HttpServletResponse response) {
        DeparturesResponse resp = DeparturesResponse.rampoff(departureService.getDepartures(latitude, longitude));

        if (resp == null || resp.getStations().size() == 0) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return resp;
    }
}
