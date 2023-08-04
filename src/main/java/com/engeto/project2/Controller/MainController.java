package com.engeto.project2.Controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.engeto.project2.UserInfo;
import java.sql.*;

@RestController
public class MainController {

    @GetMapping ("/api/v1/user/{id}")
    public ResponseEntity<String> findUser(@PathVariable ("id") int userId){
        try (
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Engeto_Project2_Registration",
                    "root",
                    "EngetoJavaHeslo123*")) {
            String query = ("SELECT ID, Name, Surname FROM registrationinfo WHERE ID = ?;");
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet result = statement.executeQuery();
            if (result.next())  {
                int id = result.getInt("ID");
                String name = result.getString("Name");
                String surname = result.getString("Surname");
                UserInfo user = new UserInfo(id, name, surname);
                return ResponseEntity.ok(convertToJson(user));
            }
        } catch (SQLException | JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("User not found!");
    }




    @PostMapping ("api/v1/user")
    public String userCreation( ){
        return "UserInfo saved!";
    }

    private String convertToJson(UserInfo user) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.writeValueAsString(user);
    }

     private void getConnection (){

     }


}
