package com.engeto.project2;

import java.util.UUID;

public class UserInfo {
    private int id;
    private String name;
    private String surname;
    private String personId;
    private UUID uuid;

    public UserInfo(int id, String name, String surname, String personId, UUID uuid) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.personId = personId;
        this.uuid = uuid;
    }

    public UserInfo(int id, String name, String surname, String personId) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.personId = personId;
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

    public String getPersonId() {
        return personId;
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

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
