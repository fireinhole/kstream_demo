package io.confluent.examples.kstream.structuredjson.processors;

import io.confluent.examples.kstream.structuredjson.models.Address;
import io.confluent.examples.kstream.structuredjson.models.Client;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressProcessor implements Processor<GenericRecord, GenericRecord> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressProcessor.class);

    private KeyValueStore<String, Client> stateStore;
    private ProcessorContext context;

    @Override
    public void init(ProcessorContext context) {
        stateStore = (KeyValueStore<String, Client>)context.getStateStore("clients");
        this.context = context;
    }

    @Override
    public void process(GenericRecord key, GenericRecord value) {
        Address address = new Address(value);
        String clientID = value.get("client_id").toString();
        String addressID = value.get("id").toString();

        Client client = stateStore.get(clientID);
        if (client == null) {
            LOGGER.error("No client found...");
            return;
        }

        client.getAddresses().put(addressID, address);

        stateStore.put(clientID, client);

        this.context.commit();
    }

    @Override
    public void close() {

    }
}
