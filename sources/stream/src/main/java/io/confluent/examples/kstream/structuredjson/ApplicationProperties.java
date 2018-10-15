package io.confluent.examples.kstream.structuredjson;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component("ApplicationProperties")
@PropertySource("classpath:application.properties")
@ConfigurationProperties()
public class ApplicationProperties {
    private String bootstrap;
    private String schema_registry;
    private String client_id;
    private String client_group_id;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_group_id() {
        return client_group_id;
    }

    public void setClient_group_id(String client_group_id) {
        this.client_group_id = client_group_id;
    }

    public String getBootstrap() {

        return bootstrap;
    }

    public void setBootstrap(String bootstrap) {
        this.bootstrap = bootstrap;
    }

    public String getSchema_registry() {
        return schema_registry;
    }

    public void setSchema_registry(String schema_registry) {
        this.schema_registry = schema_registry;
    }
}
