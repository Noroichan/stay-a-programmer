package com.stay_a_programmer.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stay_a_programmer.dto.CartItemDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartItemListJsonMapper {
    private ObjectMapper objectMapper;

    public CartItemListJsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ArrayList<CartItemDTO> toObjectList(List<String> jsonList) {
        return new ArrayList<>(jsonList.stream().map(json -> {
            try {
                return objectMapper.readValue(json, CartItemDTO.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }).toList());
    }

    public List<String> toJsonList(List<CartItemDTO> objectList) {
        return objectList.stream().map(object -> {
            try {
                return objectMapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }).toList();
    }
}
