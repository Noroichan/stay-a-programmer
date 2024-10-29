package com.stay_a_programmer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemModificationDTO(
        @NotNull(message = "Product id is required")
        long id,
        
        @NotNull(message = "Amount is required")
        @Min(value = 1, message = "Amount must be at least 1")
        int amount
) {}
