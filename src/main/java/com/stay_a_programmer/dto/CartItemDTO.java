package com.stay_a_programmer.dto;

public class CartItemDTO {
    private long id;
    private String name;
    private int price;
    private int amount;

    public CartItemDTO(long id, String name, int price, int amount) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CartItemDTO)) return false;
        return this.getId() == ((CartItemDTO) obj).getId();
    }
}
