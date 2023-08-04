package com.engeto.project2.Service;

import com.engeto.project2.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository <User,Integer>{
    @Query("SELECT u FROM User u WHERE u.ID = ?1")
    User findByID(int id);

    @Query("SELECT * FROM registrationinfo")
    List<User> selectAll();
}
