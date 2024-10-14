package com.stay_a_programmer.service;

import com.stay_a_programmer.entity.ProductEntity;
import com.stay_a_programmer.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductEntity> list() {
        return productRepository.findAll();
    }

    public ProductEntity getById(Long id) {
        return productRepository.findById(id).orElse(null); // throw error later
    }
}
