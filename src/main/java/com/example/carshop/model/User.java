package com.example.carshop.model;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private int phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private String password;

    public User() {
    }

    public User(Long id, String name, String email, int phoneNumber, UserRole role, UserStatus status, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;
        this.password = password;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public int getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(int phoneNumber) { this.phoneNumber = phoneNumber; }

    public UserRole getRole() { return role; }

    public void setRole(UserRole role) { this.role = role; }

    public UserStatus getStatus() { return status; }

    public void setStatus(UserStatus status) { this.status = status; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
}
