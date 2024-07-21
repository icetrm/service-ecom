package com.cooperation.ecom.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
@Data
@Entity
@Table(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Double price;
    @Column(name = "discount_percentage")
    private Double discountPercentage;
    private Double rating;
    private Integer stock;
    private String brand;
    @Column(name = "category_id")
    private Long categoryID;
    @Column(name = "create_author")
    private String createAuthor;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_author")
    private String updateAuthor;
    @Column(name = "update_date")
    private Date updateDate;

    public Product() {
    }

    public Product(String title, String description, Double price, Double discountPercentage, Double rating, Integer stock, String brand, Long categoryID, String createAuthor, Date createDate) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.rating = rating;
        this.stock = stock;
        this.brand = brand;
        this.categoryID = categoryID;
        this.createAuthor = createAuthor;
        this.createDate = createDate;
    }
}
