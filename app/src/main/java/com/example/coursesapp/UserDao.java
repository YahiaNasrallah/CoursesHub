package com.example.coursesapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM User WHERE Email = :email AND Password = :password")
    User getUser(String email, String password);

    @Query("SELECT * FROM User WHERE email = :email")
    User getUserByEmail(String email);

    @Query("SELECT * FROM User WHERE username = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM User WHERE id = :id")
    User getUserByid(long id);

    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Query("DELETE FROM User WHERE id = :id")
    void deleteUser(long id);

    @Query("UPDATE User SET username = :username, email = :email, password = :password WHERE id = :id")
    void updateUser(long id, String username, String email, String password);

    @Update
    void updateUser(User user);
}
