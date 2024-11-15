package com.stay_a_programmer.service;

import com.stay_a_programmer.dao.ProductDao;
import com.stay_a_programmer.dto.ProductDTO;
import com.stay_a_programmer.entity.ProductEntity;
import com.stay_a_programmer.exception.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductServiceTests {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private static List<ProductEntity> productEntities;


    @BeforeAll
    public static void beforeAll() {
        productEntities = new ArrayList<>(){{
            add(new ProductEntity((long)1, "test1", 10, new Date(), new Date(), false));
            add(new ProductEntity((long)2, "test2", 20, new Date(), new Date(), false));
            add(new ProductEntity((long)3, "test3", 10, new Date(), new Date(), false));
        }};
    }

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListing() {
        Mockito.when(this.productDao.findAll()).thenReturn(productEntities);

        var products = this.productService.list();

        Assertions.assertThat(products).isInstanceOf(List.class);
        Assertions.assertThat(products.getFirst()).isInstanceOf(ProductDTO.class);
        Assertions.assertThat(products)
                .extracting("id")
                .containsExactlyInAnyOrder(
                        productEntities.stream().map(ProductEntity::getId).toArray(Long[]::new)
                );
    }

    @Test
    public void testGetById() {
        long id = 1;

        Mockito.when(this.productDao.findById(id)).thenReturn(productEntities.getFirst());

        var product = this.productService.getById(id);

        Assertions.assertThat(product).isInstanceOf(ProductDTO.class);
        Assertions.assertThat(product.id()).isEqualTo(productEntities.getFirst().getId());
    }

    @Test
    public void testGetByIdWithNotFound() {
        long id = 4;

        Mockito.when(this.productDao.findById(id)).thenReturn(null);

        Assertions.assertThatThrownBy(() -> this.productService.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageMatching("PRODUCT_NOT_FOUND");
    }
}
