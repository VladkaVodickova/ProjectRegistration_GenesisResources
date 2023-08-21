package com.engeto.project2.service;

import com.engeto.project2.UserInfo;
import com.engeto.project2.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserInfo getUserInfoById(int userId, boolean detail) {
        try (Connection con = getConnection()) {
            String query;
            if (detail) {
                query = "SELECT * FROM registrationinfo WHERE ID = ?;";
            } else {
                query = "SELECT ID, Name, Surname FROM registrationinfo WHERE ID = ?;";
            }
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
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
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<UserInfo> getAllUsers(boolean detail) {
        List<UserInfo> users = new ArrayList<>();

        try (Connection con = getConnection()) {
            String query;
            if (detail) {
                query = "SELECT * FROM registrationinfo;";
            } else {
                query = "SELECT ID, Name, Surname FROM registrationinfo;";
            }
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet result = statement.executeQuery();


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
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }

    public UserInfo postCreateUser(UserInfo newUser) {
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newUser;
    }

    public boolean putUpdateUser(UserInfo updatedUser) {
        try (Connection con = getConnection()) {
            String query = "UPDATE registrationinfo SET Name = ?, Surname = ? WHERE ID = ?;";
            PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, updatedUser.getName());
            statement.setString(2, updatedUser.getSurname());
            statement.setInt(3, updatedUser.getId());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUserById (int userId){
        try (Connection con = getConnection()){
            String query = "DELETE FROM registrationinfo WHERE ID = ?;";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, userId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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

    public boolean checkUseOfPersonID(String personID) {
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

    public static String convertToJson(Object data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.writeValueAsString(data);
    }
}