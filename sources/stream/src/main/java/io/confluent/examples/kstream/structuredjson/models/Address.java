package io.confluent.examples.kstream.structuredjson.models;

import org.apache.avro.generic.GenericRecord;

public class Address {
    private String address;
    private String city;
    private String postalcode;
    private String country;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Address() {}
    public Address(GenericRecord record) {
        this.address = record.get("address").toString();
        this.city = record.get("city").toString();
        this.postalcode = record.get("postalcode").toString();
        this.country = record.get("country").toString();
    }
}
