package io.confluent.examples.kstream.structuredjson.processors;

import io.confluent.examples.kstream.structuredjson.models.Client;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientProcessor implements Processor<GenericRecord, GenericRecord> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientProcessor.class);

    private KeyValueStore<String, Client> stateStore;
    private ProcessorContext context;

    @Override
    public void init(ProcessorContext context) {
        stateStore = (KeyValueStore<String, Client>)context.getStateStore("clients");
        this.context = context;
    }

    @Override
    public void process(GenericRecord key, GenericRecord value) {
        LOGGER.info("Processing client....");

        Client client = new Client(value);
        stateStore.put(client.getId(), client);

        context.commit();
    }

    @Override
    public void close() {

    }
}
