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
    private String source_clients_topic;
    private String source_addresses_topic;
    private String source_transactions_topic;
    private String target_transactions_topics;

    public String getSource_clients_topic() {
        return source_clients_topic;
    }

    public void setSource_clients_topic(String source_clients_topic) {
        this.source_clients_topic = source_clients_topic;
    }

    public String getSource_addresses_topic() {
        return source_addresses_topic;
    }

    public void setSource_addresses_topic(String source_addresses_topic) {
        this.source_addresses_topic = source_addresses_topic;
    }

    public String getSource_transactions_topic() {
        return source_transactions_topic;
    }

    public void setSource_transactions_topic(String source_transactions_topic) {
        this.source_transactions_topic = source_transactions_topic;
    }

    public String getTarget_transactions_topics() {
        return target_transactions_topics;
    }

    public void setTarget_transactions_topics(String target_transactions_topics) {
        this.target_transactions_topics = target_transactions_topics;
    }

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
