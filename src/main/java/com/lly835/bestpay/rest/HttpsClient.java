package com.lly835.bestpay.rest;

import org.glassfish.jersey.SslConfigurator;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.UriBuilder;

public class HttpsClient extends Client {

    public HttpsClient(String host) {
        this(host, null);
    }

    public HttpsClient(String host, String project) {
        SslConfigurator sslConfig = SslConfigurator.newInstance();
        SSLContext ssl = sslConfig.createSSLContext();
        this.target = ClientBuilder.newBuilder().sslContext(ssl).build().target(UriBuilder.fromUri("https://" + host).build());
        if (project != null) {
            this.target = this.target.path(project);
        }
    }

}
