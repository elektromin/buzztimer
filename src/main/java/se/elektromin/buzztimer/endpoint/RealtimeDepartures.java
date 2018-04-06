package se.elektromin.buzztimer.endpoint;

import lombok.Data;
import org.joda.time.DateTime;

import java.util.List;

@Data
public class RealtimeDepartures {
    private int StatusCode;
    private String Message;
    private int ExecutionTime;
    private ResponseData ResponseData;

    @Data
    public static class ResponseData {
        private DateTime LatestUpdate;
        private int DataAge;
        private List<Bus> Buses;

        @Data
        public static class Bus {
            private String GroupOfLine;
            private String TransportMode;
            private String LineNumber;
            private String Destination;
            private int JourneyDirection;
            private String StopAreaName;
            private int StopAreaNumber;
            private int StopPointNumber;
            private String StopPointDesignation;
            private DateTime TimeTabledDateTime;
            private DateTime ExpectedDateTime;
            private String DisplayTime;
            private int JourneyNumber;
            private List<Deviation> Deviations;

            @Data
            private class Deviation {
                private String Consequence;
                private int ImportanceLevel;
                private String Text;
            }
        }
    }
}
