package edu.birzeit.zamilihotal.model;

public class Hotel {
    private String name;
    private String city;
    private String country;
    private double rate;

    public Hotel(String name, String city, String country, double rate) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
