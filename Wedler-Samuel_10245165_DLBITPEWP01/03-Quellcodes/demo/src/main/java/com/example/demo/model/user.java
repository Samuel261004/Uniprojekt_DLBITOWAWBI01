package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity(name="USERS")
public class user {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long USERID;
    @Column(name = "NAME")
    private String name;
    private String password;
    private String email;
    private LocalDate date;
    private boolean mod;
    private boolean verified=false;

    protected user() {}

    public user(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
    public Long getUSERID() {
        return USERID;
    }
    public void setUSERID(Long uSERID) {
        this.USERID = uSERID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public boolean isMod() {
        return mod;
    }
    public void setMod(boolean mod) {
        this.mod = mod;
    }
    public boolean isVerified() {
        return verified;
    }
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
