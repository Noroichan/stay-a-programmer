package com.stay_a_programmer.dao;

import com.stay_a_programmer.entity.ProductEntity;
import com.stay_a_programmer.mapper.ProductRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDao {
    private final JdbcTemplate jdbcTemplate;
    private final ProductRowMapper rowMapper;

    public ProductDao(JdbcTemplate jdbcTemplate, ProductRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    public List<ProductEntity> findAll() {
        return jdbcTemplate.query("SELECT * FROM products WHERE is_deleted IS FALSE", rowMapper);
    }

    public ProductEntity findById(Long id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM products WHERE id=? AND is_deleted IS FALSE",
                    rowMapper,
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
