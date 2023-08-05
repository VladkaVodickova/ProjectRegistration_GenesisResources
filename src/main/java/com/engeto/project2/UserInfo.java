package com.engeto.project2;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfo {
    @JsonProperty("id")
    private int id;
    private UUID uuid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surname;
    private String personID;


    public UserInfo(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public UserInfo(int id, String name, String surname, String personID) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.personID = personID;
    }

    @JsonCreator
    public UserInfo(
            int id,
            @JsonProperty("name") String name,
            @JsonProperty("surname") String surname,
            @JsonProperty("personID") String personID,
            UUID uuid) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.personID = personID;
        this.uuid = uuid;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
    @JsonProperty("personID")
    public String getPersonId() {
        return personID;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPersonId(String personID) {
        this.personID = personID;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
