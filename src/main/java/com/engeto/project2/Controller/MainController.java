package com.engeto.project2.Controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.engeto.project2.UserInfo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

    @GetMapping("/api/v1/users")
    public ResponseEntity<String> findAllUsers(@RequestParam(value = "detail", defaultValue = "false") boolean detail) {
        try (Connection con = getConnection()) {
            String query;
            if (detail) {
                query = "SELECT * FROM registrationinfo;";
            } else {
                query = "SELECT ID, Name, Surname FROM registrationinfo;";
            }
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet result = statement.executeQuery();

            List<UserInfo> users = new ArrayList<>();
            while (result.next()) {
                int id = result.getInt("ID");
                String name = result.getString("Name");
                String surname = result.getString("Surname");

                if (detail) {
                    String personId = result.getString("PersonId");
                    UUID uuid = UUID.fromString(result.getString("Uuid"));
                    users.add(new UserInfo(id, name, surname, personId, uuid));
                } else {
                    users.add(new UserInfo(id, name, surname));
                }
            }

            if (!users.isEmpty()) {
                try {
                    return ResponseEntity.ok(convertToJson(users));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
    }

    @PostMapping("/api/v1/user")
    public ResponseEntity<String> createUser(@RequestBody UserInfo newUser) {
        String personID = newUser.getPersonId();

        if (checkUseOfPersonID(personID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("PersonId is already being used.");
        }

        try (Connection con = getConnection()) {
            int ID = getIdForNextUser();
            UUID uuid = randomUUID();
            String query = "INSERT INTO registrationinfo (ID, Name, Surname, PersonID, Uuid) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, ID);
            statement.setString(2, newUser.getName());
            statement.setString(3, newUser.getSurname());
            statement.setString(4, newUser.getPersonId());
            statement.setString(5, uuid.toString());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    newUser.setId(generatedId);
                    newUser.setUuid(uuid);
                    try {
                        return ResponseEntity.ok(convertToJson(newUser));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while creating the user.");
    }

    @PutMapping ("/api/v1/user")
    public ResponseEntity<String> updateUser(@RequestBody UserInfo updatedUser){
        try (Connection con = getConnection()) {
            String query = "UPDATE registrationinfo SET Name = ?, Surname = ? WHERE ID = ?;";
            PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, updatedUser.getName());
            statement.setString(2, updatedUser.getSurname());
            statement.setInt(3,updatedUser.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try {
                    return ResponseEntity.ok(convertToJson(updatedUser));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while updating the user.");
    }

    @DeleteMapping ("api/v1/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable ("id") int userId){
        try (Connection con = getConnection()){
            String query = "DELETE FROM registrationinfo WHERE ID = ?;";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, userId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return ResponseEntity.status(HttpStatus.OK).body("User was deleted");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
    }

    private String convertToJson(Object data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.writeValueAsString(data);
    }

     private Connection getConnection () throws SQLException {
         return DriverManager.getConnection(
                 "jdbc:mysql://localhost:3306/Engeto_Project2_Registration",
                 "root",
                 "EngetoJavaHeslo123*");
     }

     private UUID randomUUID(){
         return UUID.randomUUID();
     }

     private int getIdForNextUser() throws SQLException {
         Connection con = getConnection();
         String getMaxIdQuery = "SELECT MAX(ID) AS MaxID FROM registrationinfo;";
         PreparedStatement getMaxIdStatement = con.prepareStatement(getMaxIdQuery);
         ResultSet maxIdResult = getMaxIdStatement.executeQuery();

         int nextId = 1;
         if (maxIdResult.next()) {
             int maxId = maxIdResult.getInt("MaxID");
             nextId = maxId + 1;
         }
         return nextId;
     }

     private boolean checkUseOfPersonID(String personID) {
         try (Connection con = getConnection()) {
             String checkQuery = "SELECT COUNT(*) FROM registrationinfo WHERE PersonID = ?";
             PreparedStatement checkStatement = con.prepareStatement(checkQuery);
             checkStatement.setString(1, personID);
             ResultSet resultSet = checkStatement.executeQuery();
             resultSet.next();
             int count = resultSet.getInt(1);
             return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
