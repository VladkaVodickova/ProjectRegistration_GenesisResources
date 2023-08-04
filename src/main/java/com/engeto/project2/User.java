package com.engeto.project2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String surname;
    private String personID;

    public User(int id, String name, String surname, String personID) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.personID = personID;
    }

    //getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPersonID() {
        return personID;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

}
