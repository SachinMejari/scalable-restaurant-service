package com.scalableservices.restaurantservice.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponse {
	 private Long restaurantId;
	    private String name;
	    private String address;
	    private String contactNo;
	    private Long ownerId;
	    private String status;
	    private boolean dineIn;
	    private boolean takeAway;

}
