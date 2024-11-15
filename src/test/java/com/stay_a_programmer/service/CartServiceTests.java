package com.stay_a_programmer.service;

import com.stay_a_programmer.dao.ProductDao;
import com.stay_a_programmer.dto.CartItemDTO;
import com.stay_a_programmer.dto.CartItemModificationDTO;
import com.stay_a_programmer.entity.ProductEntity;
import com.stay_a_programmer.exception.NotFoundException;
import com.stay_a_programmer.mapper.CartItemListJsonMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartServiceTests {
    @Mock
    private RedisTemplate<String, List<String>> redis;

    @Mock
    private ProductDao productDao;

    @Mock
    private CartItemListJsonMapper jsonMapper;

    @Mock
    private JokeService jokeService;

    @InjectMocks
    private CartService cartService;

    private static List<ProductEntity> productEntities;

    private static List<String> cart;

    private ValueOperations valueOperations;

    @BeforeAll
    public static void beforeAll() {
        productEntities = new ArrayList<>(){{
            add(new ProductEntity((long)1, "test1", 10, new Date(), new Date(), false));
            add(new ProductEntity((long)2, "test2", 20, new Date(), new Date(), false));
            add(new ProductEntity((long)3, "test3", 10, new Date(), new Date(), false));
        }};

        cart = new ArrayList<>();

    }

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        valueOperations = Mockito.mock(ValueOperations.class);
        Mockito.when(this.redis.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void testView_CartFound() {
        String id = "test";

        Mockito.when(valueOperations.get(id)).thenReturn(cart);

        var itemList = cartService.getCart(id);

        Assertions.assertThat(itemList).isInstanceOf(ArrayList.class);
        Mockito.verify(valueOperations, Mockito.times(1)).get(id);
        Mockito.verify(jsonMapper, Mockito.times(1)).toObjectList(cart);
    }

    @Test
    public void testView_CartNotFound() {
        String id = "test";

        Mockito.when(valueOperations.get(id)).thenReturn(null);

        Assertions.assertThatThrownBy(() -> this.cartService.getCart(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageMatching("CART_NOT_FOUND");
    }

    @Test
    public void testAddItem_AddToExistingCart() {
        String cartId = "test";
        long productId = 1;
        int amount = 1;
        CartItemModificationDTO newItem = new CartItemModificationDTO(productId, amount);

        Mockito.when(this.productDao.findById(productId)).thenReturn(productEntities.getFirst());
        Mockito.when(valueOperations.get(cartId)).thenReturn(cart);

        var item = this.cartService.addItem(cartId, newItem);

        Assertions.assertThat(item).isInstanceOf(CartItemDTO.class);
        Assertions.assertThat(item.getId()).isEqualTo(productId);
        Assertions.assertThat(item.getAmount()).isEqualTo(amount);
        Mockito.verify(valueOperations, Mockito.times(1)).set(cartId, cart);
    }

    @Test
    public void testAddItem_AddToNotExistingCart() {
        String cartId = "test";
        long productId = 1;
        int amount = 1;
        CartItemModificationDTO newItem = new CartItemModificationDTO(productId, amount);

        Mockito.when(this.productDao.findById(productId)).thenReturn(productEntities.getFirst());
        Mockito.when(valueOperations.get(cartId)).thenReturn(null);

        var item = this.cartService.addItem(cartId, newItem);

        Assertions.assertThat(item).isInstanceOf(CartItemDTO.class);
        Assertions.assertThat(item.getId()).isEqualTo(productId);
        Assertions.assertThat(item.getAmount()).isEqualTo(amount);
        Mockito.verify(valueOperations, Mockito.times(1)).set(cartId, cart);
    }

    @Test
    public void testAddItem_AddSameItemToCart() {
        String cartId = "test";
        long productId = 1;
        int amount = 1;
        CartItemModificationDTO newItem = new CartItemModificationDTO(productId, amount);
        ProductEntity firstProduct = productEntities.getFirst();

        Mockito.when(this.productDao.findById(productId)).thenReturn(firstProduct);
        Mockito.when(valueOperations.get(cartId)).thenReturn(cart);
        Mockito.when(jsonMapper.toObjectList(cart)).thenReturn(
                new ArrayList<>(){{
                    add(new CartItemDTO(productId, firstProduct.getName(), firstProduct.getPrice(), 1));
                }}
        );

        var item = this.cartService.addItem(cartId, newItem);

        Assertions.assertThat(item).isInstanceOf(CartItemDTO.class);
        Assertions.assertThat(item.getId()).isEqualTo(productId);
        Assertions.assertThat(item.getAmount()).isEqualTo(amount+1);
        Mockito.verify(valueOperations, Mockito.times(1)).set(cartId, cart);
    }

    @Test
    public void testAddItem_ProductNotFound() {
        String cartId = "test";
        long productId = 0;
        int amount = 1;
        CartItemModificationDTO newItem = new CartItemModificationDTO(productId, amount);

        Mockito.when(this.productDao.findById(productId)).thenReturn(null);

        Assertions.assertThatThrownBy(() -> this.cartService.addItem(cartId, newItem))
                .isInstanceOf(NotFoundException.class)
                .hasMessageMatching("PRODUCT_NOT_FOUND");
    }

    @Test
    public void testModifyItem_Success() {
        String cartId = "test";
        long productId = 1;
        int amount = 2;
        CartItemModificationDTO modifyItem = new CartItemModificationDTO(productId, amount);
        ProductEntity firstProduct = productEntities.getFirst();

        Mockito.when(this.productDao.findById(productId)).thenReturn(firstProduct);
        Mockito.when(valueOperations.get(cartId)).thenReturn(cart);
        Mockito.when(jsonMapper.toObjectList(cart)).thenReturn(
                new ArrayList<>(){{
                    add(new CartItemDTO(productId, firstProduct.getName(), firstProduct.getPrice(), 1));
                }}
        );

        var item = this.cartService.modifyItem(cartId, modifyItem);

        Assertions.assertThat(item).isInstanceOf(CartItemDTO.class);
        Assertions.assertThat(item.getId()).isEqualTo(productId);
        Assertions.assertThat(item.getAmount()).isEqualTo(amount);
        Mockito.verify(valueOperations, Mockito.times(1)).set(cartId, cart);
    }

    @Test
    public void testModifyItem_ProductNotFound() {
        String cartId = "test";
        long productId = 0;
        int amount = 1;
        CartItemModificationDTO newItem = new CartItemModificationDTO(productId, amount);

        Mockito.when(this.productDao.findById(productId)).thenReturn(null);

        Assertions.assertThatThrownBy(() -> this.cartService.modifyItem(cartId, newItem))
                .isInstanceOf(NotFoundException.class)
                .hasMessageMatching("PRODUCT_NOT_FOUND");
    }

    @Test
    public void testModifyItem_CartNotFound() {
        String cartId = "test";
        long productId = 0;
        int amount = 1;
        CartItemModificationDTO newItem = new CartItemModificationDTO(productId, amount);

        Mockito.when(this.productDao.findById(productId)).thenReturn(productEntities.getFirst());
        Mockito.when(valueOperations.get(cartId)).thenReturn(null);

        Assertions.assertThatThrownBy(() -> this.cartService.modifyItem(cartId, newItem))
                .isInstanceOf(NotFoundException.class)
                .hasMessageMatching("CART_NOT_FOUND");
    }

    @Test
    public void testModifyItem_CartItemNotFound() {
        String cartId = "test";
        long productId = 0;
        int amount = 1;
        CartItemModificationDTO newItem = new CartItemModificationDTO(productId, amount);

        Mockito.when(this.productDao.findById(productId)).thenReturn(productEntities.getFirst());
        Mockito.when(valueOperations.get(cartId)).thenReturn(cart);
        Mockito.when(jsonMapper.toObjectList(cart)).thenReturn(new ArrayList<>());

        Assertions.assertThatThrownBy(() -> this.cartService.modifyItem(cartId, newItem))
                .isInstanceOf(NotFoundException.class)
                .hasMessageMatching("CART_ITEM_NOT_FOUND");
    }

    @Test
    public void testRemoveItem_Success() {
        String cartId = "test";
        long itemId = 1;
        ProductEntity firstProduct = productEntities.getFirst();
        ArrayList<CartItemDTO> itemList = new ArrayList<>(){{
            add(new CartItemDTO(itemId, firstProduct.getName(), firstProduct.getPrice(), 1));
        }};

        Mockito.when(valueOperations.get(cartId)).thenReturn(cart);
        Mockito.when(jsonMapper.toObjectList(cart)).thenReturn(itemList);

        this.cartService.removeItem(cartId, itemId);

        Assertions.assertThat(itemList.size()).isEqualTo(0);
        Mockito.verify(valueOperations, Mockito.times(1)).set(cartId, cart);
    }

    @Test
    public void testRemoveItem_CartNotFound() {
        String cartId = "test";
        long itemId = 0;

        Mockito.when(valueOperations.get(cartId)).thenReturn(null);

        Assertions.assertThatThrownBy(() -> this.cartService.removeItem(cartId, itemId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageMatching("CART_NOT_FOUND");
    }

    @Test
    public void testRemoveItem_CartItemNotFound() {
        String cartId = "test";
        long itemId = 0;

        Mockito.when(valueOperations.get(cartId)).thenReturn(cart);
        Mockito.when(jsonMapper.toObjectList(cart)).thenReturn(new ArrayList<>());

        Assertions.assertThatThrownBy(() -> this.cartService.removeItem(cartId, itemId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageMatching("CART_ITEM_NOT_FOUND");
    }

    @Test
    public void testCheckout_Success() {
        String cartId = "test";
        ProductEntity firstProduct = productEntities.getFirst();
        ProductEntity lastProduct = productEntities.getLast();
        ArrayList<CartItemDTO> itemList = new ArrayList<>(){{
            add(new CartItemDTO(firstProduct.getId(), firstProduct.getName(), firstProduct.getPrice(), 1));
            add(new CartItemDTO(lastProduct.getId(), lastProduct.getName(), lastProduct.getPrice(), 2));
        }};
        List<String> testList = new ArrayList<>(){{ add("test"); }};
        String testJoke = "Test driven development? More like do the same thing twice";
        StringBuilder stringBuilder = new StringBuilder();

        itemList.forEach(item -> {
            stringBuilder.append(item.toString()).append("\n");
        });
        stringBuilder.append("Jokes on you no paying implemented: ").append(testJoke);

        Mockito.when(valueOperations.get(cartId)).thenReturn(testList);
        Mockito.when(jsonMapper.toObjectList(testList)).thenReturn(itemList);
        Mockito.when(jokeService.getJoke()).thenReturn(testJoke);

        var output = cartService.checkout(cartId);

        Assertions.assertThat(output).isEqualTo(stringBuilder.toString());
        Mockito.verify(valueOperations, Mockito.times(1)).getAndDelete(cartId);
    }

    @Test
    public void testCheckout_CartNotFound() {
        String cartId = "test";

        Mockito.when(valueOperations.get(cartId)).thenReturn(null);

        Assertions.assertThatThrownBy(() -> this.cartService.checkout(cartId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageMatching("CART_NOT_FOUND");
    }

    @Test
    public void testCheckout_NoItemFound() {
        String cartId = "test";

        Mockito.when(valueOperations.get(cartId)).thenReturn(new ArrayList<>());

        Assertions.assertThatThrownBy(() -> this.cartService.checkout(cartId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageMatching("NO_ITEM_WAS_FOUND");
    }
}
