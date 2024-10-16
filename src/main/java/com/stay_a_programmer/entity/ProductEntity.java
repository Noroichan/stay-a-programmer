package com.stay_a_programmer.entity;

import com.stay_a_programmer.dto.ProductDTO;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, updatable = false)
    private Integer price;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "modified_at", nullable = false)
    private Date modifiedAt;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted;

    public ProductEntity() {}

    public ProductEntity(Long id, String name, Integer price, Date createdAt, Date modifiedAt, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public ProductDTO mapToDTO () {
        return new ProductDTO(this.id, this.name, this.price);
    }
}
