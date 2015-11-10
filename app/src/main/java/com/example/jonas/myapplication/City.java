package com.example.jonas.myapplication;


public class City {
    private int id;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    private double temp;
    private String name, country;

    @Override
    public String toString() {
        return  name + " (" +country + ") " + temp + (char) 0x00B0 ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void City(int id, String name,String country){
        this.id = id;
        this.name=name;

        this.country = country;
    }


}
