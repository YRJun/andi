package com.summer.auth.config;

import co.elastic.clients.transport.TransportUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.support.HttpHeaders;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;

/**
 * @author Renjun Yu
 * @date 2024/03/26 09:59
 * {@link EsTest 和此类都可以连接ES,二选一}
 */
//@Configuration
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris}")
    private String uris;
    @Value("${spring.elasticsearch.username}")
    private String username;
    @Value("${spring.elasticsearch.password}")
    private String password;
    @Value("${spring.elasticsearch.ca-fingerprint}")
    private String caFingerprint;
    @Value("${spring.elasticsearch.ca-path}")
    private String caPath;
    @Override
    public ClientConfiguration clientConfiguration() {
        SSLContext sslContext;
        try {
            sslContext = TransportUtils.sslContextFromHttpCaCrt(new File(caPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 使用手动拼http请求到es时需要的认证信息
        //final String s = HttpHeaders.encodeBasicAuth(username, password);
        // 使用构建器来提供集群地址
        return ClientConfiguration.builder()
                // 设置连接地址
                .connectedTo(uris)
                // 启用ssl并配置CA指纹,openssl x509 -in node-1.crt -sha256 -fingerprint | grep SHA256 | sed 's/://g'
                //.usingSsl(caFingerprint)
                .usingSsl(sslContext)
                // 设置用户名密码
                .withBasicAuth(username, password)
                // 创建连接信息
                .build();
    }
}
