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

    private String JoinDate;

    private int PhoneNumber;

    private String UserImagePath;

    public User(){

    }

    public User(@NonNull String username, @NonNull String email, @NonNull String password) {
        this.Username = username;
        this.Email = email;
        this.Password = password;
    }

    public User(@NonNull String username, @NonNull String email, @NonNull String password,String joindate,int phonenumber,String userImage) {
        this.Username = username;
        this.Email = email;
        this.Password = password;
        this.JoinDate =joindate;
        this.PhoneNumber =phonenumber;
        this.UserImagePath =userImage;
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

    public String getJoinDate() {
        return JoinDate;
    }

    public void setJoinDate(String joinDate) {
        JoinDate = joinDate;
    }

    public int getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getUserImagePath() {
        return UserImagePath;
    }

    public void setUserImagePath(String userImagePath) {
        UserImagePath = userImagePath;
    }
}
