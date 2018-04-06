package se.elektromin.buzztimer.test;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import se.elektromin.buzztimer.util.DefaultEndpoint;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Server<T extends Server> {

    public static final String HTTP_LOCALHOST = "http://localhost:";
    protected ServerBootstrap bootstrap;

    private boolean isDebug = false;

    public Server() {
        this.bootstrap = ServerBootstrap.bootstrap()
                .setSocketConfig(SocketConfig.custom()
                        .setSoTimeout(100)
                        .build());
    }

    public static HttpRequestHandler newCallbackRequestHandler(final Callback callback) {
        return new HttpRequestHandler() {
            @Override
            public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
                httpResponse.setStatusCode(HttpServletResponse.SC_OK);
                callback.call();
            }
        };
    }

    public static HttpRequestHandler newRequestHandler(final String resource, final int statusCode) {
        return newRawRequestHandler(Mock.get(resource), statusCode);
    }

    public static HttpRequestHandler newRawRequestHandler(final String content, final int statusCode) {
        return new HttpRequestHandler() {
            @Override
            public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
                httpResponse.setStatusCode(statusCode);
                httpResponse.setEntity(new StringEntity(content));
            }
        };
    }

    public T handler(final String pattern, HttpRequestHandler handler) {
        bootstrap.registerHandler(pattern, handler);
        return (T) this;
    }

    public T emptyOkHandler(final String pattern) {
        bootstrap.registerHandler(pattern, newRawRequestHandler("", HttpServletResponse.SC_OK));
        return (T) this;
    }

    public T debug(final boolean isDebug) {
        this.isDebug = isDebug;
        return (T) this;
    }

    public ClosableServer start(final DefaultEndpoint... endpoints) throws Exception {
        final HttpServer server = bootstrap.create();
        server.start();

        final String serverUrl = HTTP_LOCALHOST + server.getLocalPort();
        for (DefaultEndpoint endpoint : endpoints) {
            endpoint.setBaseUri(serverUrl);
            endpoint.setDebug(isDebug);
        }
        return new ClosableServer(server);
    }

    public class ClosableServer {
        private HttpServer server;

        private ClosableServer(final HttpServer server) {
            this.server = server;
        }

        public void close() throws Exception {
            this.server.shutdown(10, TimeUnit.SECONDS);
        }

        public String getServerUrl() {
            return HTTP_LOCALHOST + server.getLocalPort();
        }
    }
}
