package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.rest.ip.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;


@SpringBootTest
@Testcontainers
class IpApiClientIntegrationTest {

    private static MockServerClient mockServerClient;

    @Autowired
    private IpApiClient underTest;

    @Container
    private static final MockServerContainer mockServer = new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.15.0"));

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("infrastructure.adapters.output.rest.ip.api.baseUrl", mockServer::getEndpoint);
    }

    @BeforeAll
    static void beforeAll() {
        mockServerClient = new MockServerClient(mockServer.getHost(), mockServer.getServerPort());
    }

    @BeforeEach
    void setUp() {
        mockServerClient.reset();
    }

    @Test
    void shouldGetIpDetails() {
        // given
        var ip = "89.64.53.187";

        mockServerClient.when(request().withMethod("GET")
                                       .withPath("/json/{ip}")
                                       .withPathParameter("ip", ip))
                        .respond(response().withStatusCode(200)
                                           .withContentType(MediaType.APPLICATION_JSON)
                                           .withBody(prepareIpApiResponse()));

        // when
        var response = underTest.ip(ip);

        // then
        assertThat(response).isEqualTo(new IpApiResponse(
            ip,
            "success",
            Double.valueOf("50.0595"),
            Double.valueOf("19.9558")
        ));
    }

    @Test
    void shouldHandleInvalidQuery() {
        // given
        var invalidIp = "1189.64.53.187";

        mockServerClient.when(request().withMethod("GET")
                                       .withPath("/json/{ip}")
                                       .withPathParameter("ip", invalidIp))
                        .respond(response().withStatusCode(200)
                                           .withContentType(MediaType.APPLICATION_JSON)
                                           .withBody(prepareIpApiResponseWithInvalidQuery()));

        // when
        var response = underTest.ip(invalidIp);

        // then
        assertThat(response).isEqualTo(new IpApiResponse(invalidIp, "fail", null, null));
    }

    private String prepareIpApiResponse() {
        return """
            {
                "query": "89.64.53.187",
                "status": "success",
                "continent": "Europe",
                "continentCode": "EU",
                "country": "Poland",
                "countryCode": "PL",
                "city": "Krakow",
                "zip": "31-532",
                "lat": 50.0595,
                "lon": 19.9558
            }
            """;
    }

    private String prepareIpApiResponseWithInvalidQuery() {
        return """
            {
                "status": "fail",
                "message": "invalid query",
                "query": "1189.64.53.187"
            }
            """;
    }

    @Import({
        JacksonAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        RestClientAutoConfiguration.class
    })
    @ComponentScan("com.github.madaaraisok.guard.bot.infrastructure.adapters.output.rest.ip.api")
    @Configuration
    static class TestConfig {

    }

}