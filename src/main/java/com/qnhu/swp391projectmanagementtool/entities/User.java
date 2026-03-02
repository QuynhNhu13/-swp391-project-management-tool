package com.qnhu.swp391projectmanagementtool.entities;

import com.qnhu.swp391projectmanagementtool.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String username;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Integer yob;
    private String phoneNumber;

    public User() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }


    public Integer getYob() {
        return yob;
    }

    public void setYob(Integer yob) {
        this.yob = yob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void login() {
    }

    public void logout() {
    }

    public void manageStudentGroups() {
    }

    public void viewRequirements() {
    }

    public void viewTasks() {
    }

    public void assignTaskToMember() {
    }

    public void commitToGitHub() {
    }
}
