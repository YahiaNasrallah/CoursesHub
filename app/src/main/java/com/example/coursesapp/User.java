package com.example.coursesapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    @NonNull
    private String Email;
    @NonNull
    private String Username;
    @NonNull
    private String Password;

    public User(){

    }

    public User(@NonNull String username, @NonNull String email, @NonNull String password) {
        this.Username = username;
        this.Email = email;
        this.Password = password;
    }

    @NonNull
    public String getUsername() {
        return Username;
    }

    public void setUsername(@NonNull String username) {
        Username = username;
    }

    @NonNull
    public String getEmail() {
        return Email;
    }

    public void setEmail(@NonNull String email) {
        Email = email;
    }

    @NonNull
    public String getPassword() {
        return Password;
    }

    public void setPassword(@NonNull String password) {
        Password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
