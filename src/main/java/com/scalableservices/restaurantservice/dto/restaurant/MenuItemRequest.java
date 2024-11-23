package com.scalableservices.restaurantservice.dto.restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemRequest {
    private String itemName;
    private String itemDescription;
    private BigDecimal itemPrice;
    private Boolean isAvailable;
}