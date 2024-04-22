package com.summer.auth.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.io.File;

/**
 * @author Renjun Yu
 * @date 2024/04/21 18:19
 * {@link ElasticsearchConfig 和此类都可以连接ES,二选一}
 */
@Component
@Slf4j
public class EsTest {
    @Value("${spring.elasticsearch.uris}")
    private String uri;
    @Value("${spring.elasticsearch.username}")
    private String username;
    @Value("${spring.elasticsearch.password}")
    private String password;
    @Value("${spring.elasticsearch.ca-fingerprint}")
    private String caFingerprint;
    @Value("${spring.elasticsearch.ca-path}")
    private String caPath;
    /**
     * 连接es7.17
     */
    //@Bean
    public ElasticsearchClient connectEs7_17() {
        try {
            final String[] host = uri.split(":");
            // Create the low-level client
            RestClient restClient = RestClient.builder(new HttpHost(host[0], Integer.parseInt(host[1])))
                    .build();
            // Create the transport with a Jackson mapper
            ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
            // And create the API client
            ElasticsearchClient client = new ElasticsearchClient(transport);
            log.info("elasticsearch7.17 connect success,uri:{}", uri);
            return client;
        } catch (Exception e) {
            log.error("elasticsearch7.17 connect error,uri:{}", uri, e);
            return null;
        }
    }

    /**
     * 连接es8.11
     */
    //@Bean("elasticsearchClient")
    public ElasticsearchClient connectEs8_11() {
        try {
            final String[] host = uri.split(":");
            SSLContext sslContext = TransportUtils.sslContextFromHttpCaCrt(new File(caPath));
            BasicCredentialsProvider credProv = new BasicCredentialsProvider();
            credProv.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
            RestClient restClient = RestClient
                    .builder(new HttpHost(host[0], Integer.parseInt(host[1]), "https"))
                    .setHttpClientConfigCallback(hc -> hc
                            .setSSLContext(sslContext)
                            .setDefaultCredentialsProvider(credProv)
                    )
                    .build();
            // Create the transport and the API client
            ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
            ElasticsearchClient client = new ElasticsearchClient(transport);
            log.info("elasticsearch8.11 connect success,uri:{}", uri);
            return client;
        } catch (Exception e) {
            log.error("elasticsearch8.11 connect error,uri:{}", uri, e);
            return null;
        }
    }

    /**
     * 连接本地的es8.11
     */
    //@Bean
    public ElasticsearchClient connectLocalEs8_11() {
        try {
            final String[] host = uri.split(":");
            SSLContext sslContext = TransportUtils.sslContextFromCaFingerprint(caFingerprint);
            BasicCredentialsProvider credProv = new BasicCredentialsProvider();
            credProv.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
            RestClient restClient = RestClient
                    .builder(new HttpHost(host[0], Integer.parseInt(host[1]), "https"))
                    .setHttpClientConfigCallback(hc -> hc
                            .setSSLContext(sslContext)
                            .setDefaultCredentialsProvider(credProv)
                    )
                    .build();

            // Create the transport and the API client
            ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
            ElasticsearchClient client = new ElasticsearchClient(transport);
            log.info("elasticsearch8.11Local connect success,uri:{}", uri);
            return client;
        } catch (Exception e) {
            log.error("elasticsearch8.11Local connect error,uri:{}", uri, e);
            return null;
        }
    }
}
