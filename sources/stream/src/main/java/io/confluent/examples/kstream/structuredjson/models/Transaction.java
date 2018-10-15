package io.confluent.examples.kstream.structuredjson.models;

import org.apache.avro.generic.GenericRecord;

public class Transaction {
    private String type;
    private Double amount;
    private Client client;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Transaction() {}
    public Transaction(final GenericRecord record, final Client client) {
        this.type = record.get("type").toString();
        this.amount = (Double)record.get("amount");
        this.client = client;
    }
}
