package com.stay_a_programmer.service;

import com.stay_a_programmer.dao.ProductDao;
import com.stay_a_programmer.dto.CartItemDTO;
import com.stay_a_programmer.dto.CartItemModificationDTO;
import com.stay_a_programmer.dto.ProductDTO;
import com.stay_a_programmer.entity.ProductEntity;
import com.stay_a_programmer.exception.NotFoundException;
import com.stay_a_programmer.mapper.CartItemListJsonMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final RedisTemplate<String, List<String>> redis;
    private final ProductDao productDao;
    private final CartItemListJsonMapper jsonMapper;
    private final JokeService jokeService;

    public CartService(JokeService jokeService, CartItemListJsonMapper jsonMapper, ProductDao productDao, RedisTemplate<String, List<String>> redis) {
        this.jokeService = jokeService;
        this.jsonMapper = jsonMapper;
        this.productDao = productDao;
        this.redis = redis;
    }

    public List<CartItemDTO> getCart(String id) {
        List<String> cartJson = redis.opsForValue().get(id);

        if (cartJson == null) {
            throw new NotFoundException("CART_NOT_FOUND");
        }

        return jsonMapper.toObjectList(cartJson);
    }

    public CartItemDTO addItem(String id, CartItemModificationDTO newItem) {
        ProductEntity productEntity = this.productDao.findById(newItem.id());

        if (productEntity == null) {
            throw new NotFoundException("PRODUCT_NOT_FOUND");
        }

        ProductDTO productDTO = productEntity.mapToDTO();

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
        ProductEntity productEntity = this.productDao.findById(modificationItem.id());

        if (productEntity == null) {
            throw new NotFoundException("PRODUCT_NOT_FOUND");
        }

        ProductDTO productDTO = productEntity.mapToDTO();

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

    public String checkout(String cartId) {
        List<String> cartJson = redis.opsForValue().get(cartId);

        if (cartJson == null) {
            throw new NotFoundException("CART_NOT_FOUND");
        }

        if (cartJson.isEmpty()) {
            throw new NotFoundException("NO_ITEM_WAS_FOUND");
        }

        List<CartItemDTO> cart = jsonMapper.toObjectList(cartJson);

        StringBuilder stringBuilder = new StringBuilder();
        cart.forEach(item -> {
            stringBuilder.append(item.toString()).append("\n");
        });

        stringBuilder.append("Jokes on you no paying implemented: ").append(jokeService.getJoke());

        redis.opsForValue().getAndDelete(cartId);

        return stringBuilder.toString();
    }
}
