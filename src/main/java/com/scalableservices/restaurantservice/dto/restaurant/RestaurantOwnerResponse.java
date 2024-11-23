package com.scalableservices.restaurantservice.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantOwnerResponse {
    private Long ownerId;
    private String name;
    private String mobileNumber;
    private String email;
}
