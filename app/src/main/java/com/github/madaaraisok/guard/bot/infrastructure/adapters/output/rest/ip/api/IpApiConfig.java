package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.rest.ip.api;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;


@Configuration
class IpApiConfig {

    @Bean
    ClientHttpRequestFactory ipApiRequestFactory() {
        return ClientHttpRequestFactoryBuilder.httpComponents()
                                              .withDefaultRequestConfigCustomizer(this::customizeRequestConfig)
                                              .build();
    }

    @Bean
    RestClient ipApiRestClient(RestClient.Builder builder,
                               @Value("${infrastructure.adapters.output.rest.ip.api.baseUrl}") String baseUrl,
                               ClientHttpRequestFactory ipApiRequestFactory) {
        return builder.baseUrl(baseUrl)
                      .requestFactory(ipApiRequestFactory)
                      .build();
    }

    @Bean
    IpApiClient ipApiClient(RestClient ipApiRestClient) {
        return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(ipApiRestClient))
                                      .build()
                                      .createClient(IpApiClient.class);
    }

    private void customizeRequestConfig(RequestConfig.Builder builder) {
        builder.setConnectionRequestTimeout(Timeout.ofSeconds(1))
               .setResponseTimeout(Timeout.ofSeconds(3));
    }

}
