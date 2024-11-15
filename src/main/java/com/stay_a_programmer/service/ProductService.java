package com.stay_a_programmer.service;

import com.stay_a_programmer.dao.ProductDao;
import com.stay_a_programmer.dto.ProductDTO;
import com.stay_a_programmer.entity.ProductEntity;
import com.stay_a_programmer.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductDTO> list() {
        return productDao.findAll().stream().map(ProductEntity::mapToDTO).toList();
    }

    public ProductDTO getById(Long id) {
        ProductEntity productEntity = productDao.findById(id);

        if (productEntity == null) {
            throw new NotFoundException("PRODUCT_NOT_FOUND");
        }

        return productEntity.mapToDTO();
    }
}
