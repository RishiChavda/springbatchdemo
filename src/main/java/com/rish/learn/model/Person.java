package com.rish.learn.model;

public class Person {

    private int id;
    private String fullname;
    private String address;
    private String biography;

    public Person() {
    }

    public Person(int id, String fullname, String address, String biography) {
        this.id = id;
        this.fullname = fullname;
        this.address = address;
        this.biography = biography;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return this.fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address= address;
    }

    public String getBiography() {
        return this.biography;
    }

    public void setBiography(String biography) {
        this.biography= biography;
    }

    @Override
    public String toString() {
        return "fullname: " + fullname + ", address: " + address + ", biography: " + biography;
    }

}