package com.scalableservices.restaurantservice.controller;

import com.scalableservices.restaurantservice.dto.common.ApiResponse;
import com.scalableservices.restaurantservice.dto.common.ErrorMessage;
import com.scalableservices.restaurantservice.dto.restaurant.*;
import com.scalableservices.restaurantservice.exception.ServiceException;
import com.scalableservices.restaurantservice.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
	@Autowired
	RestaurantService restaurantService;

	// Endpoint to register a new restaurant
	@PostMapping("/owner/register")
	public ApiResponse<RestaurantOwnerResponse> registerOwner(@RequestBody RestaurantOwnerRequest ownerRequest) {
		try {
			RestaurantOwnerResponse response = restaurantService.registerRestaurantOwner(ownerRequest);
			return ApiResponse.<RestaurantOwnerResponse>builder().status("success").data(response).build();
		}catch (ServiceException e) {
			return ApiResponse.<RestaurantOwnerResponse>builder().status("failed")
					.error(ErrorMessage.builder().error(e.getMessage()).description(e.getMessage()).build())
					.build();
		}
		catch (Exception e) {
			return ApiResponse
					.<RestaurantOwnerResponse>builder().status("failed").error(ErrorMessage.builder()
							.error("Error while registering restaurant owner").description(e.getMessage()).build())
					.build();
		}
	}

	@PostMapping("/register")
	public ApiResponse<RestaurantResponse> registerRestaurant(@RequestBody RestaurantRequest ownerRequest, @RequestHeader(value = "X-UserType", required = true) String userType) {
		try {
			if(!userType.equalsIgnoreCase("restaurant_owner")) {
				throw new ServiceException(HttpStatus.UNAUTHORIZED, "Only restaurant owners are allowed to register restaurants");
			}
			RestaurantResponse response = restaurantService.registerRestaurant(ownerRequest);
			return ApiResponse.<RestaurantResponse>builder().status("success").data(response).build();
		} catch (Exception e) {
			return ApiResponse
					.<RestaurantResponse>builder().error(ErrorMessage.builder()
							.error("Error while registering restaurant owner").description(e.getMessage()).build())
					.build();
		}
	}

	// Endpoint to add menu items to an existing restaurant
	@PostMapping("/{restaurantId}/menu")
	public ApiResponse<List<MenuItemResponse>> addMenuToRestaurant(@PathVariable Long restaurantId,
																   @RequestBody List<MenuItemRequest> menuItems
			, @RequestHeader(value = "X-UserType", required = true) String userType) {
		try {
			if(!userType.equalsIgnoreCase("restaurant_owner")) {
				throw new ServiceException(HttpStatus.UNAUTHORIZED, "Only restaurant owners are allowed to add menu items");
			}
			List<MenuItemResponse> response = restaurantService.addMenuToRestaurant(restaurantId, menuItems);
			return ApiResponse.<List<MenuItemResponse>>builder().status("success").data(response).build();
		}catch (ServiceException e) {
			return ApiResponse.<List<MenuItemResponse>>builder().status("failed")
					.error(ErrorMessage.builder().error("Menu item addition failed").description(e.getMessage()).build())
					.build();
		}
		catch (Exception e) {
			return ApiResponse.<List<MenuItemResponse>>builder().status("failed")
					.error(ErrorMessage.builder().error("Menu item addition failed").description(e.getMessage()).build())
					.build();
		}
	}

	@PutMapping("/{restaurantId}/update-restaurant")
	public ApiResponse<RestaurantResponse> updateRestaurant(@PathVariable Long restaurantId,
															@RequestBody RestaurantRequest updatedRestaurant
			, @RequestHeader(value = "X-UserType", required = true) String userType) {
		try {
			if(!userType.equalsIgnoreCase("restaurant_owner")) {
				throw new ServiceException(HttpStatus.UNAUTHORIZED, "Only restaurant owners are allowed to update restaurants");
			}
			RestaurantResponse response = restaurantService.updateRestaurant(restaurantId, updatedRestaurant);
			return ApiResponse.<RestaurantResponse>builder().status("success").data(response).build();
		} catch (Exception e) {
			return ApiResponse.<RestaurantResponse>builder()
					.error(ErrorMessage.builder().error("Restaurant update failed").description(e.getMessage()).build())
					.build();
		}
	}

	@PutMapping("/menu/{itemId}")
	public ApiResponse<MenuItemResponse> updateMenuItem(@PathVariable Long itemId,
			@RequestBody MenuItemRequest restaurantMenu
			, @RequestHeader(value = "X-UserType", required = true) String userType) {
		try {
			if(!userType.equalsIgnoreCase("restaurant_owner")) {
				throw new ServiceException(HttpStatus.UNAUTHORIZED, "Only restaurant owners are allowed to update menu items");
			}
			MenuItemResponse response = restaurantService.updateMenuItem(itemId, restaurantMenu);
			return ApiResponse.<MenuItemResponse>builder().status("success").data(response).build();
		} catch (Exception e) {
			return ApiResponse.<MenuItemResponse>builder()
					.error(ErrorMessage.builder().error("Menu item update failed").description(e.getMessage()).build())
					.build();
		}
	}
}