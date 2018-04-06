package se.elektromin.buzztimer.endpoint;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * https://stackoverflow.com/questions/7668507/gson-handle-object-or-array
 */
public class StopLocationTypeAdapter implements JsonDeserializer<List<StopLocation>> {
    public List<StopLocation> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) {
        List<StopLocation> vals = new ArrayList<>();
        if (json.isJsonArray()) {
            for (JsonElement e : json.getAsJsonArray()) {
                vals.add((StopLocation) ctx.deserialize(e, StopLocation.class));
            }
        } else if (json.isJsonObject()) {
            vals.add((StopLocation) ctx.deserialize(json, StopLocation.class));
        } else {
            throw new RuntimeException("Unexpected JSON type: " + json.getClass());
        }
        return vals;
    }
}
