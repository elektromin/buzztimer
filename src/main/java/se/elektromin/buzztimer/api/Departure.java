package se.elektromin.buzztimer.api;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import se.elektromin.buzztimer.endpoint.RealtimeDepartures;

import java.util.Date;

@Slf4j
@Data
public class Departure {
    @NonNull
    private String lineNumber, destination, displayTime;
    private Date plannedDeparture, expectedDeparture;

    public static Departure rampoff(final RealtimeDepartures.ResponseData.Bus bus) {
        final Departure dep = new Departure(bus.getLineNumber(), bus.getDestination(), bus.getDisplayTime());
        dep.setPlannedDeparture(toDate(bus.getTimeTabledDateTime()));
        dep.setExpectedDeparture(toDate(bus.getExpectedDateTime()));
        return dep;
    }

    private static Date toDate(final DateTime dateTime) {
        final Date date = dateTime != null ? dateTime.withZoneRetainFields(DateTimeZone.UTC).toDate() : null;
        LOG.debug("from: " + dateTime + ", to: " + date);
        return date;
    }
}
