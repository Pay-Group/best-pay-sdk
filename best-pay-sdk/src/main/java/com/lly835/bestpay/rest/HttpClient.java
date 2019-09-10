package com.lly835.bestpay.rest;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.UriBuilder;

public class HttpClient extends Client {

    public HttpClient(String host, Integer port) {
        this(host, port, null);
    }

    public HttpClient(String host, Integer port, String project) {
        this(host, port, project, null, null);
    }

    public HttpClient(String host, Integer port, String project, Integer maxTotal, Integer maxPerRoute) {
        if (port != null && port > 0) {
            host = host + ":" + port;
        }

        maxTotal = maxTotal == null || maxTotal < 20 ? 200 : maxTotal;
        maxPerRoute = maxPerRoute == null || maxPerRoute < 2 || maxPerRoute > maxTotal ? maxTotal / 2 : maxPerRoute;
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(maxPerRoute);
        ClientConfig config = new ClientConfig().property(ApacheClientProperties.CONNECTION_MANAGER, cm)
                .connectorProvider(new ApacheConnectorProvider());

        this.target = ClientBuilder.newClient(config).target(UriBuilder.fromUri("http://" + host).build());
        if (project != null) {
            this.target = this.target.path(project);
        }
    }

}
