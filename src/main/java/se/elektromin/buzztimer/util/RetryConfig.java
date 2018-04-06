package se.elektromin.buzztimer.util;

import lombok.Data;

@Data
public class RetryConfig {

    static final int DEFAULT_NO_OF_RETRIES = 5;
    static final int DEFAULT_WAIT_UNTIL_RETRY_MS = 500;
    static final int DEFAULT_WAIT_INCREMENT_MS = 500;

    static final int DEFAULT_READ_TIMEOUT_MS = 5000;
    static final int DEFAULT_CONNECT_TIMEOUT_MS = 30000;

    private int readTimeout;
    private int connectionTimeout;
    private int retries;
    private int waitUntilRetryMs;
    private int waitIncrementMs;

    public static RetryConfig getDefault() {
        RetryConfig config = new RetryConfig();
        config.readTimeout = DEFAULT_READ_TIMEOUT_MS;
        config.connectionTimeout = DEFAULT_CONNECT_TIMEOUT_MS;
        config.retries = DEFAULT_NO_OF_RETRIES;
        config.waitIncrementMs = DEFAULT_WAIT_INCREMENT_MS;
        config.waitUntilRetryMs = DEFAULT_WAIT_UNTIL_RETRY_MS;

        return config;
    }
}