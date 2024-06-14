package com.summer.common.config;

import com.summer.common.repository.RepositoryService;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.concurrent.TimeUnit;

/**
 * Rest Client配置类，对于每个http Interface，都需要配置一个RestClient
 * 注意：RestClient的ClientHttpRequestFactory不推荐使用{@link org.springframework.http.client.SimpleClientHttpRequestFactory}
 * @author Renjun Yu
 * @date 2024/05/31 14:07
 */
@Configuration
public class RestClientConfig {
    private String baseUrl = "http://127.0.0.1:1001";
    private String basePath = "";
    private Integer readTimeout = 60 * 1000;

    /**
     * 自定义HttpClient，除responseTimeout之外，其他配置都使用{@link HttpClients#createSystem()}相同的默认配置
     * @return 自定义的httpClient
     */
    public HttpClient customHttpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                RegistryBuilder.<ConnectionSocketFactory>create()
                        .register(URIScheme.HTTP.id, PlainConnectionSocketFactory.getSocketFactory())
                        .register(URIScheme.HTTPS.id, SSLConnectionSocketFactory.getSystemSocketFactory())
                        .build());
        RequestConfig requestConfig = RequestConfig.custom()
                .setResponseTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .build();
        return HttpClientBuilder.create()
                .useSystemProperties()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
    }

    @Bean("repositoryService")
    public RepositoryService repositoryService() {
        RestClient restClient = RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory(this.customHttpClient()))
                .baseUrl(baseUrl + basePath)
                .defaultStatusHandler(HttpStatusCode::isError, ((request, response) -> {
                    throw new RestClientException("http service 异常");
                }))
                .build();
        final RestClientAdapter adapter = RestClientAdapter.create(restClient);
        final HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(RepositoryService.class);
    }
}
