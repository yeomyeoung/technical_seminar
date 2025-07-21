package com.example.ZeroTrust.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "employee")  // DB의 테이블명
public class User {

    @Id
    @Column(name = "employeeid")   // ✅ PK 컬럼과 매핑
    private String userID;

    @Column(name = "employee_name")  // ✅ 이름 컬럼과 매핑
    private String userName;

    @Column(name = "email")          // ✅ 이메일 컬럼과 매핑
    private String email;

    @Column(name = "password")       // ✅ 비밀번호 컬럼과 매핑
    private String password;

    @Column(name = "role")           // ✅ 역할 컬럼과 매핑
    private String role;
}
