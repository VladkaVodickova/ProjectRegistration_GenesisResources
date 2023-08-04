package com.engeto.project2.Controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.engeto.project2.UserInfo;
import java.sql.*;
import java.util.UUID;

@RestController
public class MainController {

    @GetMapping ("/api/v1/user/{id}")
    public ResponseEntity<String> findUser(@PathVariable ("id") int userId, @RequestParam (value = "detail", defaultValue = "false") boolean detail){
        try (Connection con = getConnection())
             {
            String query;
             if (detail) {
                 query = "SELECT * FROM registrationinfo WHERE ID = ?;";
             } else {
                 query = "SELECT ID, Name, Surname FROM registrationinfo WHERE ID = ?;";
             }
             PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet result = statement.executeQuery();
            if (result.next())  {
                int id = result.getInt("ID");
                String name = result.getString("Name");
                String surname = result.getString("Surname");
                UserInfo user;
                if (detail) {
                    String personId = result.getString("PersonId");
                    UUID uuid = UUID.fromString(result.getString("Uuid"));
                    user = new UserInfo(id, name, surname, personId, uuid);
                } else {
                    user = new UserInfo(id, name, surname);
                }
                try {
                    return ResponseEntity.ok(convertToJson(user));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }
             } catch (SQLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
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

     private Connection getConnection () throws SQLException {
         return DriverManager.getConnection(
                 "jdbc:mysql://localhost:3306/Engeto_Project2_Registration",
                 "root",
                 "EngetoJavaHeslo123*");
     }


}
