package se.elektromin.buzztimer.api;

import lombok.Data;
import lombok.NonNull;

@Data
public class Hello {
    @NonNull
    private String greeting;
}
