package com.scalableservices.restaurantservice.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRequest {
	private String name;
	private String address;
	private String contactNo;
	private String openingDays;
	private String openingTime;
	private String closingTime;
	private String email;
	private boolean dineIn;
	private boolean takeAway;
	private Long ownerId;
}
