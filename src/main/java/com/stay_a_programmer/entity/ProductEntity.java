package com.stay_a_programmer.entity;

import com.stay_a_programmer.dto.ProductDTO;

import java.util.Date;

public class ProductEntity {
    private Long id;

    private String name;

    private Integer price;

    private Date createdAt;

    private Date modifiedAt;

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
