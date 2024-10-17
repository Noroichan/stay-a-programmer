package com.stay_a_programmer.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    // TODO add response type
    @GetMapping("/view")
    public ResponseEntity<?> viewCart(@CookieValue(value = "cartId") long cartId, HttpServletResponse response) {
        // TODO implement
        return null;
    }

    // TODO add response type
    @PostMapping("/addItem")
    public ResponseEntity<?> addItem(
            @CookieValue(value = "cartId") long cartId,
            // TODO add dto
            @RequestBody Object item,
            HttpServletResponse response
    ) {
        // TODO implement
        return null;
    }

    // TODO add response type
    @PutMapping("/removeItem/{id}")
    public ResponseEntity<?> removeItem(@CookieValue(value = "cartId") long cartId, @PathVariable long id) {
        // TODO implement
        return null;
    }

    // TODO add response type
    @PutMapping("/modifyItem/{id}")
    public ResponseEntity<?> modifyItem(
            @CookieValue(value = "cartId") long cartId,
            @PathVariable long id,
            // TODO add dto
            @RequestBody Object modifiedItem
    ) {
        // TODO implement
        return null;
    }

    // TODO add response type
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@CookieValue(value = "id") long id, HttpServletResponse response) {
        // TODO implement
        return null;
    }
}
