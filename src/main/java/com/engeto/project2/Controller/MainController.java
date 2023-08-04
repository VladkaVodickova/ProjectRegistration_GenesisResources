package com.engeto.project2.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.engeto.project2.UserInfo;
import java.sql.*;

@RestController
public class MainController {

    @GetMapping ("/api/v1/user/id")
    public ResponseEntity<String> findUser(){
        try (
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/Engeto_Project2_Registration",
                        "root",
                        "EngetoJavaHeslo123*")) {
            Statement statement = con.createStatement();
            statement.executeQuery("SELECT * FROM registrationinfo;");
            ResultSet result = statement.getResultSet();
            if (result.next())  {
                int id = result.getInt("ID");
                String name = result.getString("Name");
                String surname = result.getString("Surname");
                String personId = result.getString("PersonID");
                UserInfo user = new UserInfo(id, name, surname, personId );
                return ResponseEntity.ok(convertToJson(user));
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String convertToJson(UserInfo user) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(user);
    }


    @PostMapping ("api/v1/user")
    public String userCreation( ){
        return "UserInfo saved!";
    }


}
