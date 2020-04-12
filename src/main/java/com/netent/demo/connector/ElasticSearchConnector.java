package com.netent.demo.connector;


import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ElasticSearchConnector {

  @Value("${maxConnPerRoute:5}")
  private int maxConnPerRoute;

  @Value("${maxTotalConn:20}")
  private int maxTotalConn;

  @Value("${elasticsearch.host}")
  private String esEndPoint;

  @Value("${elasticsearch.password}")
  private String password;

  @Value("${elasticsearch.username}")
  private String username;

  @Bean
  public JestClient getEsConnector() {

    JestClientFactory factory = new JestClientFactory() {

      @Override
      protected HttpClientBuilder configureHttpClient(HttpClientBuilder builder) {
        builder = super.configureHttpClient(builder);

        boolean requestSentRetryEnabled = true;

        builder.setRetryHandler(new DefaultHttpRequestRetryHandler(
            3,
            requestSentRetryEnabled));

        return builder;
      }
    };

    factory.setHttpClientConfig(new HttpClientConfig
        .Builder(esEndPoint).defaultCredentials(username, password)
        .multiThreaded(true)
        .defaultMaxTotalConnectionPerRoute(maxConnPerRoute)
        .maxTotalConnection(maxTotalConn)
        .build());

    JestClient client = factory.getObject();

    return client;
  }
}
