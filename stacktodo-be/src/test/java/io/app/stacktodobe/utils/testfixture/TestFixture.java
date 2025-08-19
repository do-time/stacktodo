package io.app.stacktodobe.utils.testfixture;


import org.springframework.boot.test.web.client.LocalHostUriTemplateHandler;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;

public record TestFixture(
        TestRestTemplate client
) {
    public static TestFixture create(Environment environment) {
        var client = new TestRestTemplate();
        var uriTemplateRenderer = new LocalHostUriTemplateHandler(environment);
        client.setUriTemplateHandler(uriTemplateRenderer);
        return new TestFixture(client);
    }
}
