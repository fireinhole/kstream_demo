package io.confluent.examples.kstream.structuredjson.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JsonSerializer<T> implements Serializer<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonSerializer.class);

    private final Class<T> serializedClass;

    public JsonSerializer(final Class<T> serializedClass) {
        this.serializedClass = serializedClass;
    }

    @Override
    public void configure(Map configs, boolean isKey) {
        configs.put("serializedClass", serializedClass);
    }

    @Override
    public byte[] serialize(String topic, T data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void close() {

    }
}
