package se.elektromin.buzztimer.test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Slf4j
public class Mock {

    public static String get(final String resource) {
        final URL url = Resources.getResource(resource);
        try {
            return Resources.toString(url, Charsets.UTF_8);
        } catch (IOException e) {
            LOG.error("Could not get content from " + url.toExternalForm(), e);
            throw new IllegalStateException(e);
        }
    }

    public static InputStream getAsInputStream(final String resource) throws IOException {
        final URL url = Resources.getResource(resource);
        return url.openStream();
    }

}
