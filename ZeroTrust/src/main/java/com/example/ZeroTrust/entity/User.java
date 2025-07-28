package com.example.ZeroTrust.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@Data
@Entity
@Table(name = "employee")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "employeeid")
    private String userID;

    @Column(name = "employee_name")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    public String getPassword() {
        return this.password;
    }
    
    public String getRole() {
        return this.role;
    }
    
}

