package se.elektromin.buzztimer.endpoint;

import com.google.common.primitives.Ints;
import lombok.Data;
import lombok.NonNull;

@Data
public class StopLocation implements Comparable {
    private String name, id, lat, lon, dist;

    public String getSiteId() {
        if (id != null && id.length() >= 5) {
            final Integer oi = Ints.tryParse(id.substring(id.length() - 5, id.length()));
            if (oi != null) {
                return oi.toString();
            }
        }
        throw new RuntimeException("Cannot parse site id: " + id);
    }

    public Integer getDistance() {
        return dist != null ? Ints.tryParse(dist) : null;
    }

    @Override
    public int compareTo(@NonNull final Object o) {
        final StopLocation other = (StopLocation) o;
        if (getDistance() != null && other.getDistance() != null) {
            return getDistance().compareTo(other.getDistance());
        }
        return 0;
    }
}
