package com.cooperation.ecom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="role")
public class Role {

    @Id
    private Long id;
    private String title;
    @Column(name = "create_author")
    private String createAuthor;
    @Column(name = "create_date")
    private Date createDate;

    public Role() {
    }

    public Role(Long id) {
        this.id = id;
    }
}
