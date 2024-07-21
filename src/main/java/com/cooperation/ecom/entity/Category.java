package com.cooperation.ecom.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "create_author")
    private String createAuthor;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_author")
    private String updateAuthor;
    @Column(name = "update_date")
    private Date updateDate;

    public Category() {
    }

    public Category(String name, String createAuthor, Date createDate) {
        this.name = name;
        this.createAuthor = createAuthor;
        this.createDate = createDate;
    }

}
