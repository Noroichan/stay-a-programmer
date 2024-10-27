package com.stay_a_programmer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stay_a_programmer.dto.CartItemDTO;
import com.stay_a_programmer.dto.CartItemModificationDTO;
import com.stay_a_programmer.dto.ProductDTO;
import com.stay_a_programmer.exception.NotFoundException;
import com.stay_a_programmer.mapper.CartItemListJsonMapper;
import com.stay_a_programmer.repository.ProductRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private RedisTemplate<String, List<String>> redis;
    private ProductRepository productRepository;
    private CartItemListJsonMapper jsonMapper;

    public CartService(RedisTemplate<String, List<String>> redis, ProductRepository productRepository, CartItemListJsonMapper jsonMapper) {
        this.redis = redis;
        this.productRepository = productRepository;
        this.jsonMapper = jsonMapper;
    }

    public List<CartItemDTO> getCart(String id) {
        List<String> cartJson = redis.opsForValue().get(id);

        if (cartJson == null) {
            throw new NotFoundException("CART_NOT_FOUND");
        }

        return jsonMapper.toObjectList(cartJson);
    }

    public CartItemDTO addItem(String id, CartItemModificationDTO newItem) {
        ProductDTO productDTO = this.productRepository.findById(newItem.id())
                .orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND"))
                .mapToDTO();

        List<String> cartJson = redis.opsForValue().get(id);

        if (cartJson == null) {
            cartJson = new ArrayList<>();
        }

        CartItemDTO item = new CartItemDTO(newItem.id(), productDTO.name(), productDTO.price(), newItem.amount());
        List<CartItemDTO> cart = jsonMapper.toObjectList(cartJson);

        int index = cart.indexOf(item);

        if (index == -1) {
            cart.add(item);
        } else {
            item.setAmount(item.getAmount() + cart.get(index).getAmount());
            cart.set(index, item);
        }

        redis.opsForValue().set(id, jsonMapper.toJsonList(cart));

        return item;
    }

    public CartItemDTO modifyItem(String id, CartItemModificationDTO modificationItem) {
        ProductDTO productDTO = this.productRepository.findById(modificationItem.id())
                .orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND"))
                .mapToDTO();

        List<String> cartJson = redis.opsForValue().get(id);

        if (cartJson == null) {
            throw new NotFoundException("CART_NOT_FOUND");
        }

        List<CartItemDTO> cart = jsonMapper.toObjectList(cartJson);

        CartItemDTO item = new CartItemDTO(
                modificationItem.id(),
                productDTO.name(),
                productDTO.price(),
                modificationItem.amount()
        );

        int index = cart.indexOf(item);

        if (index == -1) {
            throw new NotFoundException("CART_ITEM_NOT_FOUND");
        }

        cart.set(index, item);

        redis.opsForValue().set(id, jsonMapper.toJsonList(cart));

        return item;
    }

    public void removeItem(String cartId, long itemId) {
        List<String> cartJson = redis.opsForValue().get(cartId);

        if (cartJson == null) {
            throw new NotFoundException("CART_NOT_FOUND");
        }

        List<CartItemDTO> cart = jsonMapper.toObjectList(cartJson);

        if (cart.removeIf(item -> item.getId() == itemId)) {
            redis.opsForValue().set(cartId, jsonMapper.toJsonList(cart));
        } else {
            throw new NotFoundException("CART_ITEM_NOT_FOUND");
        }
    }
}
