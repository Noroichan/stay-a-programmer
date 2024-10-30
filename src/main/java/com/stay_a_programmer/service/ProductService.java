package com.stay_a_programmer.service;

import com.stay_a_programmer.dto.ProductDTO;
import com.stay_a_programmer.entity.ProductEntity;
import com.stay_a_programmer.exception.NotFoundException;
import com.stay_a_programmer.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> list() {
        return productRepository.findAll().stream().map(ProductEntity::mapToDTO).toList();
    }

    public ProductDTO getById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND")).mapToDTO();
    }
}
