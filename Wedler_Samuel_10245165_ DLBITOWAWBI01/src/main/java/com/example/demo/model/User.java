package com.example.demo.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "FONTSIZE")
    private String fontSize = "medium";

    @Column(name = "HIGHCONTRAST")
    private boolean highContrast = false;

    public User() {
    }

    public User(String username, String password, Role role, String fontSize, boolean highContrast) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fontSize = fontSize;
        this.highContrast = highContrast;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }   

    public String getPassword() {
        return password;
    }


    public Role getRole() {
        return role;
    }  

    public String getFontSize() {
        return fontSize;
    }

    public boolean isHighContrast() {
        return highContrast;
    } 

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public void setHighContrast(boolean highContrast) {
        this.highContrast = highContrast;
    }
}