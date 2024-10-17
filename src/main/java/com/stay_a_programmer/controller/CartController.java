package com.stay_a_programmer.controller;

import com.stay_a_programmer.dto.CartItemDTO;
import com.stay_a_programmer.dto.CartItemModificationDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @GetMapping("/view")
    public ResponseEntity<List<CartItemDTO>> viewCart(@CookieValue(value = "cartId") long cartId, HttpServletResponse response) {
        // TODO implement
        return null;
    }

    @PostMapping("/addItem")
    public ResponseEntity<CartItemDTO> addItem(
            @CookieValue(value = "cartId") long cartId,
            @Valid @RequestBody CartItemModificationDTO newItem,
            HttpServletResponse response
    ) {
        // TODO implement
        return null;
    }

    @PutMapping("/modifyItem")
    public ResponseEntity<CartItemDTO> modifyItem(
            @CookieValue(value = "cartId") long cartId,
            @Valid @RequestBody CartItemModificationDTO modificationItem
    ) {
        // TODO implement
        return null;
    }

    @PutMapping("/removeItem/{id}")
    public ResponseEntity<String> removeItem(@CookieValue(value = "cartId") long cartId, @PathVariable long id) {
        // TODO implement
        return null;
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@CookieValue(value = "id") long id, HttpServletResponse response) {
        // TODO implement
        return null;
    }
}
