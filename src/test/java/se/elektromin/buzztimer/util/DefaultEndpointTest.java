package se.elektromin.buzztimer.util;

import lombok.Cleanup;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.junit.Before;
import org.junit.Test;
import se.elektromin.buzztimer.test.Server;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DefaultEndpointTest {

    private static final Boolean ENABLE_DEBUG = false;

    private DefaultEndpoint endpoint;

    @Before
    public void setUp() {
        endpoint = new DefaultEndpoint() {
        };
        endpoint.setRetries(1);
    }

    @Test
    public void test() throws Exception {
        @Cleanup Server.ClosableServer closable = new Server().debug(ENABLE_DEBUG).emptyOkHandler("/").start(endpoint);
        final DefaultEndpoint.EndpointResponse response = invoke(endpoint);
        assertEquals(HttpServletResponse.SC_OK, response.getStatusCode());
    }

    @Test
    public void testInternalError() throws Exception {
        @Cleanup Server.ClosableServer closable = new Server().debug(ENABLE_DEBUG).emptyOkHandler("/unknown").start(endpoint);
        final DefaultEndpoint.EndpointResponse response = invoke(endpoint);
        assertEquals(HttpServletResponse.SC_NOT_IMPLEMENTED, response.getStatusCode());
    }

    @Test(expected = EndpointException.class)
    public void testException() throws Exception {
        @Cleanup Server.ClosableServer closable = new Server().debug(ENABLE_DEBUG).handler("/", getErrorRequestHandler()).start(endpoint);
        invoke(endpoint);
    }

    private DefaultEndpoint.EndpointResponse invoke(DefaultEndpoint endpoint) throws Exception {
        final Invocation.Builder invocationBuilder = endpoint.getTarget().request(MediaType.TEXT_PLAIN);
        final Invocation invocation = invocationBuilder.buildGet();
        return endpoint.invoke(invocation);
    }

    private HttpRequestHandler getErrorRequestHandler() {
        return new HttpRequestHandler() {
            @Override
            public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
                throw new RuntimeException("Expected error");
            }
        };
    }
}
