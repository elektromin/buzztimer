package se.elektromin.buzztimer.util;

import com.google.common.base.Strings;
import com.google.common.primitives.Doubles;

public class Utils {

    public static Double parseDouble(String str, Double defaultValue) {
        if (Strings.isNullOrEmpty(str)) {
            return defaultValue;
        } else {
            Double aDouble = Doubles.tryParse(str);
            return aDouble != null ? aDouble : defaultValue;
        }
    }

}
