package io.confluent.examples.kstream.structuredjson.models;

import org.apache.avro.generic.GenericRecord;

import java.util.HashMap;
import java.util.Map;

public class Client {
    private String id;
    private String first_name;
    private String last_name;
    private Integer age;
    private String gender;
    private final Map<String, Address> addresses = new HashMap<>();

    public Map<String, Address> getAddresses() {
        return addresses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Client() {}
    public Client(GenericRecord record) {
        this.id = record.get("id").toString();
        this.first_name = record.get("first_name").toString();
        this.last_name = record.get("last_name").toString();
        this.age = (Integer)record.get("age");
        this.gender = record.get("gender").toString();
    }
}
