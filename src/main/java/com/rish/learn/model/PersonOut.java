package com.rish.learn.model;

import java.util.List;
import java.util.ArrayList;

public class PersonOut {

    private int id;
    private String fullname;
    private List<String> address;
    private String biography;

    public PersonOut() {
    }

    public PersonOut(int id, String fullname, List<String> address, String biography) {
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

    public List<String> getAddress() {
        return this.address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public String getBiography() {
        return this.biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public String toString() {
        return "fullname: " + fullname + ", address: " + address + ", biography: " + biography;
    }

}