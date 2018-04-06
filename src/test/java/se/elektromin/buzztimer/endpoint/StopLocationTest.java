package se.elektromin.buzztimer.endpoint;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StopLocationTest {
    @Test
    public void getSiteId() throws Exception {
        final StopLocation sl = new StopLocation();

        sl.setId("300109600");
        assertEquals("9600", sl.getSiteId());

        sl.setId("300119600");
        assertEquals("19600", sl.getSiteId());
    }

    @Test(expected = RuntimeException.class)
    public void getSiteId_invalid1() {
        final StopLocation sl = new StopLocation();
        sl.setId("9600");
        sl.getSiteId();
    }

    @Test(expected = RuntimeException.class)
    public void getSiteId_invalid2() {
        final StopLocation sl = new StopLocation();
        sl.setId("a9600");
        sl.getSiteId();
    }

    @Test(expected = RuntimeException.class)
    public void getSiteId_invalid3() {
        final StopLocation sl = new StopLocation();
        sl.setId(null);
        sl.getSiteId();
    }

}