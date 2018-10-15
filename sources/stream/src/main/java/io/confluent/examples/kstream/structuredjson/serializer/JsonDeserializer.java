package io.confluent.examples.kstream.structuredjson.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class JsonDeserializer<T> implements Deserializer<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonDeserializer.class);

    private final Class<T> deserializedClass;

    public JsonDeserializer(final Class<T> deserializedClass) {
        this.deserializedClass = deserializedClass;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(data, deserializedClass);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void close() {

    }
}
