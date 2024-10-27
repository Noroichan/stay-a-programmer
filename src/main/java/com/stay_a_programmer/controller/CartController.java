package com.stay_a_programmer.controller;

import com.stay_a_programmer.dto.CartItemDTO;
import com.stay_a_programmer.dto.CartItemModificationDTO;
import com.stay_a_programmer.service.CartService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController extends BaseController {

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/view")
    public ResponseEntity<List<CartItemDTO>> viewCart(@CookieValue("cartId") String cartId) {
        return ResponseEntity.ok(cartService.getCart(cartId));
    }

    @PostMapping("/addItem")
    public ResponseEntity<CartItemDTO> addItem(
            @CookieValue(value = "cartId", defaultValue = "") String cartId,
            @Valid @RequestBody CartItemModificationDTO newItem,
            HttpServletResponse response
    ) {
        if (cartId.isEmpty()) {
            cartId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("cartId", cartId);

            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24); // TODO environment variables

            response.addCookie(cookie);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(cartId, newItem));
    }

    @PutMapping("/modifyItem")
    public ResponseEntity<CartItemDTO> modifyItem(
            @CookieValue("cartId") String cartId,
            @Valid @RequestBody CartItemModificationDTO modificationItem
    ) {
        return ResponseEntity.ok(cartService.modifyItem(cartId, modificationItem));
    }

    @PutMapping("/removeItem/{id}")
    public ResponseEntity<String> removeItem(@CookieValue("cartId") String cartId, @PathVariable long id) {
        cartService.removeItem(cartId, id);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@CookieValue("cartId") String cartId, HttpServletResponse response) {
        // TODO implement
        return null;
    }
}
