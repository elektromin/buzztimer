package se.elektromin.buzztimer.test;

import java.util.concurrent.atomic.AtomicBoolean;

public class WasCalledCallback implements Callback {
    private AtomicBoolean wasCalled = new AtomicBoolean(false);

    @Override
    public void call() {
        wasCalled.set(true);
    }

    public boolean isCalled() {
        return wasCalled.get();
    }
}
