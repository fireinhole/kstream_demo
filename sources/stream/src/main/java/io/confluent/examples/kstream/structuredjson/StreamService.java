package io.confluent.examples.kstream.structuredjson;

import io.confluent.examples.kstream.structuredjson.models.Client;
import io.confluent.examples.kstream.structuredjson.models.Transaction;
import io.confluent.examples.kstream.structuredjson.processors.AddressProcessor;
import io.confluent.examples.kstream.structuredjson.processors.ClientProcessor;
import io.confluent.examples.kstream.structuredjson.processors.TransactionProcessor;
import io.confluent.examples.kstream.structuredjson.serializer.JsonDeserializer;
import io.confluent.examples.kstream.structuredjson.serializer.JsonSerializer;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class StreamService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamService.class);

    private class Supplier<T extends Processor<GenericRecord, GenericRecord>> implements ProcessorSupplier<GenericRecord, GenericRecord> {

        private final T processor;

        private Supplier(T processor) {
            this.processor = processor;
        }

        @Override
        public Processor<GenericRecord, GenericRecord> get() {
            return this.processor;
        }
    }
    private final ApplicationProperties applicationProperties;
    private KafkaStreams kafkaStreams;

    public StreamService(@Autowired ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public void start() {
        LOGGER.info("Starting Stream service....");

        JsonSerializer<Client> clientJsonSerializer = new JsonSerializer<>(Client.class);
        JsonDeserializer<Client> clientJsonDeserializer = new JsonDeserializer<>(Client.class);
        JsonSerializer<Transaction> transactionJsonSerializer = new JsonSerializer<>(Transaction.class);
        JsonDeserializer<Transaction> transactionJsonDeserializer = new JsonDeserializer<>(Transaction.class);

        final String bootstrapServers = applicationProperties.getBootstrap();
        final String schemaRegistry = applicationProperties.getSchema_registry();

        final Properties streamsConfiguration = new Properties();

        // Give the Streams application a unique name.  The name must be unique in the Kafka cluster
        // against which the application is run.
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationProperties.getClient_group_id());
        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, applicationProperties.getClient_id());
        // Where to find Kafka broker(s).
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        streamsConfiguration.put("schema.registry.url", schemaRegistry);

        // Specify default (de)serializers for record keys and for record values.
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, GenericAvroSerde.class);
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class);

        streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
        streamsConfiguration.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);

        Topology builder = new Topology();

        builder.addSource("clients", "cdc.pocdb.clients")
                .addProcessor("clientProcessor", new Supplier<>(new ClientProcessor()), "clients")

                .addSource("addresses", "cdc.pocdb.addresses")
                .addProcessor("addressProcessor", new Supplier<>(new AddressProcessor()), "addresses")

                .addSource("transactions", "cdc.pocdb.transactions")
                .addProcessor("transactionsProcessor", new Supplier<>(new TransactionProcessor()), "transactions")

                .addStateStore(Stores.keyValueStoreBuilder(
                        Stores.persistentKeyValueStore("clients"),
                        Serdes.String(),
                        Serdes.serdeFrom(clientJsonSerializer, clientJsonDeserializer)
                ), "clientProcessor", "addressProcessor", "transactionsProcessor")

                .addSink("enrichedTransactions", "transactions", new StringSerializer(), transactionJsonSerializer, "transactionsProcessor");


        LOGGER.info(builder.describe().toString());

        kafkaStreams = new KafkaStreams(builder, streamsConfiguration);
        kafkaStreams.start();

        LOGGER.info("Stream service started....");
    }

    public void stop() {
        LOGGER.info("Stopping Stream Service....");
        kafkaStreams.close();
        LOGGER.info("Stream service stopped....");
    }

}
