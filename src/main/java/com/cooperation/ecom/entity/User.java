package com.cooperation.ecom.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String mobile;
    private String email;
    private String gender;
    private Integer age;
    @Column(name = "last_login")
    private Date lastLogin;
    @Column(name = "create_author")
    private String createAuthor;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_author")
    private String updateAuthor;
    @Column(name = "update_date")
    private Date updateDate;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;
}
