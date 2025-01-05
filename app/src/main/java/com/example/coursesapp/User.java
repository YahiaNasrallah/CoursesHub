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
    private String FirstName;

    private String LastName;
    @NonNull
    private String Password;

    private String JoinDate;

    private int PhoneNumber;

    private String UserImagePath;

    public User(){

    }

    public User(@NonNull String firstname, @NonNull String email, @NonNull String password) {
        this.FirstName = firstname;
        this.Email = email;
        this.Password = password;
    }

    public User(@NonNull String firstname, @NonNull String email, @NonNull String password,String joindate,int phonenumber,String userImage) {
        this.FirstName = firstname;
        this.Email = email;
        this.Password = password;
        this.JoinDate =joindate;
        this.PhoneNumber =phonenumber;
        this.UserImagePath =userImage;
    }
    @NonNull
    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(@NonNull String firstName) {
        FirstName = firstName;
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

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
