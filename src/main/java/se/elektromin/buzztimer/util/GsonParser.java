package se.elektromin.buzztimer.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.joda.time.DateTime;
import se.elektromin.buzztimer.endpoint.StopLocation;
import se.elektromin.buzztimer.endpoint.StopLocationTypeAdapter;

import java.util.List;

public class GsonParser {
    public Gson getGson() {
        return getBuilder().create();
    }

    protected GsonBuilder getBuilder() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter())
                .registerTypeAdapter(new TypeToken<List<StopLocation>>() {
                        }.getType(),
                        new StopLocationTypeAdapter());
    }
}