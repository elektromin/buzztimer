package se.elektromin.buzztimer.util;

import com.github.rholder.retry.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.logging.LoggingFeature;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Slf4j
public abstract class DefaultEndpoint {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(DefaultEndpoint.class.getName());

    @Setter
    private String baseUri;
    @Setter
    @Getter
    private GsonParser gsonParser;
    @Setter
    private boolean debug;
    @Setter
    private RetryConfig retryConfig;

    public DefaultEndpoint() {
        retryConfig = RetryConfig.getDefault();
    }

    protected WebTarget getTarget() {
        return getTarget(baseUri, retryConfig);
    }

    protected WebTarget getTarget(final String overriddenBaseUri) {
        return getTarget(overriddenBaseUri, retryConfig);
    }

    private WebTarget getTarget(final String uri, final RetryConfig retryConfig) {
        final ClientConfig config = new ClientConfig();

        if (debug) {
            final Feature feature = new LoggingFeature(LOGGER, Level.INFO, null, null);
            config.register(feature);
        }

        final Client client = ClientBuilder.newClient(config);
        client.property(ClientProperties.CONNECT_TIMEOUT, retryConfig.getConnectionTimeout());
        client.property(ClientProperties.READ_TIMEOUT, retryConfig.getReadTimeout());

        return client.target(uri);
    }

    protected EndpointResponse invoke(final Invocation invocation) {
        return invoke(invocation, retryConfig);
    }

    private EndpointResponse handleResponse(Response response) {
        try {
            final EndpointResponse endpointResponse = new EndpointResponse();
            final String message = getResponseAsString(response);
            endpointResponse.setMessage(message);
            endpointResponse.setStatusCode(response.getStatus());
            endpointResponse.setHeaders(response.getStringHeaders());
            return endpointResponse;
        } catch (IOException e) {
            throw new EndpointException("Could not handle response.", e);
        }
    }

    protected String getResponseAsString(final Response response) throws IOException {
        return response.readEntity(String.class);
    }

    private EndpointResponse invoke(final Invocation invocation, final RetryConfig retryConfig) {

        final Callable<EndpointResponse> callable = new Callable<EndpointResponse>() {
            @Override
            public EndpointResponse call() throws IOException {
                LOG.debug("Calling invokation");
                final Response response = invocation.invoke(Response.class);
                return handleResponse(response);
            }
        };

        // Don't retry if we get a RateLimitExceededException.
        final Retryer<EndpointResponse> retryer = RetryerBuilder.<EndpointResponse>newBuilder()
                .retryIfExceptionOfType(ResponseProcessingException.class)
                .retryIfExceptionOfType(ProcessingException.class)
                .retryIfExceptionOfType(WebApplicationException.class)
                .withWaitStrategy(WaitStrategies.incrementingWait(retryConfig.getWaitUntilRetryMs(), TimeUnit.MILLISECONDS,
                        retryConfig.getWaitIncrementMs(), TimeUnit.MILLISECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(retryConfig.getRetries()))
                .build();

        try {
            return retryer.call(callable);
        } catch (RetryException e) {
            // We have tried everything we can. Fail and let task retry if required.
            throw new EndpointException(String.format("Could not request resource '%s'. Giving up after '%d' retries.", baseUri, retryConfig.getRetries()), e);
        } catch (ExecutionException e) {
            throw new EndpointException(String.format("Could not request resource '%s'.", baseUri), e);
        }
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        if (connectionTimeout != null) {
            retryConfig.setConnectionTimeout(connectionTimeout);
        }
    }

    public void setReadTimeout(Integer readTimeout) {
        if (readTimeout != null) {
            retryConfig.setReadTimeout(readTimeout);
        }
    }

    public void setRetries(Integer retries) {
        if (retries != null) {
            retryConfig.setRetries(retries);
        }
    }

    public void setWaitIncrementMs(Integer waitIncrementMs) {
        if (waitIncrementMs != null) {
            retryConfig.setWaitIncrementMs(waitIncrementMs);
        }
    }

    public void setWaitUntilRetryMs(Integer waitUntilRetryMs) {
        if (waitUntilRetryMs != null) {
            retryConfig.setWaitUntilRetryMs(waitUntilRetryMs);
        }
    }

    @Data
    public static class EndpointResponse implements Serializable {
        private static final long serialVersionUID = 1L;
        private int statusCode;
        private String message;
        private MultivaluedMap<String, String> headers;
    }
}
