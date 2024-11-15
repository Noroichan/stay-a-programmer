package com.stay_a_programmer.mapper;

import com.stay_a_programmer.entity.ProductEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProductRowMapper implements RowMapper<ProductEntity> {
    @Override
    public ProductEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ProductEntity(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getInt("price"),
                rs.getDate("created_at"),
                rs.getDate("modified_at"),
                rs.getBoolean("is_deleted")
        );
    }
}
