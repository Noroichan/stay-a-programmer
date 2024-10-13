package com.stay_a_programmer.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {
    public List<String> list() {
        return Arrays.asList("Hello", " ", "World", "!");
    }

    public String getById(String id) {
        return id + ": This is a product";
    }
}
