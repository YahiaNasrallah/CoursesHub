package com.example.coursesapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM User WHERE email = :email AND password = :password")
    User getUser(String email, String password);

    @Query("SELECT * FROM User WHERE email = :email")
    User getUserByEmail(String email);

    @Query("SELECT * FROM User WHERE username = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Query("DELETE FROM User WHERE id = :id")
    void deleteUser(long id);

    @Query("UPDATE User SET username = :username, email = :email, password = :password WHERE id = :id")
    void updateUser(long id, String username, String email, String password);
}
