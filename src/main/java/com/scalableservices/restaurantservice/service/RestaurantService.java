package com.scalableservices.restaurantservice.service;


import com.scalableservices.restaurantservice.dto.restaurant.*;
import com.scalableservices.restaurantservice.exception.ServiceException;
import com.scalableservices.restaurantservice.model.Restaurant;
import com.scalableservices.restaurantservice.model.RestaurantMenu;
import com.scalableservices.restaurantservice.model.RestaurantOwner;
import com.scalableservices.restaurantservice.repository.RestaurantMenuRepository;
import com.scalableservices.restaurantservice.repository.RestaurantOwnerRepository;
import com.scalableservices.restaurantservice.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RestaurantService {
	@Autowired
	private RestaurantRepository restaurantRepository;
	@Autowired
	private RestaurantOwnerRepository restaurantOwnerRepository;
	@Autowired
	private RestaurantMenuRepository restaurantMenuRepository;


	public Restaurant getRestaurantById(Long restaurantId) {
		// Fetch restaurant details from database
		return restaurantRepository.findById(restaurantId).orElse(null);
	}

	public List<Restaurant> getAllRestaurants() {
		return restaurantRepository.findAll();
	}

	public RestaurantResponse registerRestaurant(RestaurantRequest restaurantRequest) {
		try {
			// Check if a restaurant with the given contact number already exists
			Restaurant existingRestaurant = restaurantRepository.findByContactNo(restaurantRequest.getContactNo());
			if (existingRestaurant != null) {
				log.error("Restaurant with contact number {} already exists", restaurantRequest.getContactNo());
				throw new ServiceException(HttpStatus.BAD_REQUEST,
						"Restaurant with contact number " + restaurantRequest.getContactNo() + " already exists");
			}
			RestaurantOwner restaurantOwner = restaurantOwnerRepository.findById(restaurantRequest.getOwnerId()).orElse(null);

			// Create a new restaurant
			Restaurant newRestaurant = Restaurant.builder().name(restaurantRequest.getName())
					.address(restaurantRequest.getAddress()).contactNo(restaurantRequest.getContactNo())
					.openingDays(restaurantRequest.getOpeningDays()).openingTime(restaurantRequest.getOpeningTime())
					.closingTime(restaurantRequest.getClosingTime()).dineIn(restaurantRequest.isDineIn())
					.takeAway(restaurantRequest.isTakeAway()).owner(restaurantOwner).isDeleted(false).isArchived(false)
					.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

			// Save the new restaurant to the database
			restaurantRepository.save(newRestaurant);

			log.info("Restaurant added successfully with contact number: {}", restaurantRequest.getContactNo());

			return RestaurantResponse.builder().restaurantId(newRestaurant.getId()).name(newRestaurant.getName())
					.address(newRestaurant.getAddress()).contactNo(newRestaurant.getContactNo()).status("ACTIVE")
					.dineIn(newRestaurant.getDineIn()).takeAway(newRestaurant.getTakeAway()).ownerId(restaurantOwner.getId()).build();

		} catch (ServiceException e) {
			log.error("Error while registering restaurant: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Error while registering restaurant: {}", e.getMessage());
			throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
		}
	}

	public List<MenuItemResponse> addMenuToRestaurant(Long restaurantId, List<MenuItemRequest> menuItems) {
		// Find the restaurant by ID
		Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
				() -> new ServiceException(HttpStatus.NOT_FOUND, "Restaurant with ID " + restaurantId + " not found"));

		List<RestaurantMenu> restaurantMenuItems = menuItems.stream().map(menuItemRequest -> RestaurantMenu.builder().restaurant(restaurant).itemName(menuItemRequest.getItemName())
                .itemDescription(menuItemRequest.getItemDescription()).itemPrice(menuItemRequest.getItemPrice())
                .isAvailable(menuItemRequest.getIsAvailable()).isDeleted(false).isArchived(false)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build()).collect(Collectors.toList());

		// Save the list of RestaurantMenu items
		restaurantMenuRepository.saveAll(restaurantMenuItems);

		List<MenuItemResponse> response = restaurantMenuItems.stream().map(menuItem -> {

			return MenuItemResponse.builder().id(menuItem.getId()).itemName(menuItem.getItemName())
					.itemDescription(menuItem.getItemDescription()).itemPrice(menuItem.getItemPrice())
					.isAvailable(menuItem.getIsAvailable()).build();
		}).collect(Collectors.toList());

		return response;
	}

	public RestaurantResponse updateRestaurant(Long restaurantId, RestaurantRequest restaurantRequest) {
		try {
			// Check if the restaurant exists
			Restaurant existingRestaurant = restaurantRepository.findById(restaurantId)
					.orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Restaurant not found"));

			// Check if another restaurant with the same contact number exists (but not the
			// current one)
			Restaurant duplicateRestaurant = restaurantRepository
					.findByContactNoAndIdNot(restaurantRequest.getContactNo(), restaurantId);
			if (duplicateRestaurant != null) {
				log.error("Restaurant with contact number {} already exists", restaurantRequest.getContactNo());
				throw new ServiceException(HttpStatus.BAD_REQUEST,
						"Restaurant with contact number " + restaurantRequest.getContactNo() + " already exists");
			}

			// Update the restaurant details
			existingRestaurant.setName(restaurantRequest.getName());
			existingRestaurant.setAddress(restaurantRequest.getAddress());
			existingRestaurant.setOpeningDays(restaurantRequest.getOpeningDays());
			existingRestaurant.setOpeningTime(restaurantRequest.getOpeningTime());
			existingRestaurant.setClosingTime(restaurantRequest.getClosingTime());
			existingRestaurant.setDineIn(restaurantRequest.isDineIn());
			existingRestaurant.setTakeAway(restaurantRequest.isTakeAway());
			existingRestaurant.setUpdatedAt(LocalDateTime.now());

			// Save the updated restaurant to the database
			restaurantRepository.save(existingRestaurant);

			log.info("Restaurant updated successfully with ID: {}", restaurantId);

			return RestaurantResponse.builder().restaurantId(existingRestaurant.getId())
					.name(existingRestaurant.getName()).address(existingRestaurant.getAddress())
					.contactNo(existingRestaurant.getContactNo()).status("ACTIVE").dineIn(existingRestaurant.getDineIn())
					.takeAway(existingRestaurant.getTakeAway()).build();

		} catch (ServiceException e) {
			log.error("Error while updating restaurant: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Error while updating restaurant: {}", e.getMessage());
			throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
		}
	}

	public MenuItemResponse updateMenuItem(Long itemId, MenuItemRequest menuItems) {
		try {
				RestaurantMenu existingMenuItem = restaurantMenuRepository.findById(itemId)
						.orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND,
								"Menu item with ID " + itemId + " not found"));

				existingMenuItem.setItemName(menuItems.getItemName());
				existingMenuItem.setItemDescription(menuItems.getItemDescription());
				existingMenuItem.setItemPrice(menuItems.getItemPrice());
				existingMenuItem.setIsAvailable(menuItems.getIsAvailable());
				existingMenuItem.setUpdatedAt(LocalDateTime.now());

			restaurantMenuRepository.save(existingMenuItem);

			return MenuItemResponse.builder().id(existingMenuItem.getId()).itemName(existingMenuItem.getItemName())
						.itemDescription(existingMenuItem.getItemDescription()).itemPrice(existingMenuItem.getItemPrice())
						.isAvailable(existingMenuItem.getIsAvailable()).build();

		} catch (ServiceException e) {
			log.error("Error while updating menu items: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Error while updating menu items: {}", e.getMessage());
			throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
		}
	}

	public RestaurantOwnerResponse registerRestaurantOwner(RestaurantOwnerRequest restaurantOwnerRequest) {
		try {
			// Check if a restaurant owner with the given contact number already exists
			RestaurantOwner existingRestaurantOwner = restaurantOwnerRepository
					.findByMobileNumber(restaurantOwnerRequest.getMobileNumber());

			if(existingRestaurantOwner != null) {
				log.error("Restaurant owner with contact number {} already exists", restaurantOwnerRequest.getMobileNumber());
				throw new ServiceException(HttpStatus.BAD_REQUEST, "Restaurant owner with contact number " + restaurantOwnerRequest.getMobileNumber() + " already exists");
			}
            // Create a new restaurant owner
			RestaurantOwner newRestaurantOwner = RestaurantOwner.builder().name(restaurantOwnerRequest.getName())
					.mobileNumber(restaurantOwnerRequest.getMobileNumber()).email(restaurantOwnerRequest.getEmail())
					.isDeleted(false).isArchived(false)
					.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

			// Save the new restaurant owner to the database
			restaurantOwnerRepository.save(newRestaurantOwner);

			log.info("Restaurant owner added successfully with contact number: {}", restaurantOwnerRequest.getMobileNumber());

			return RestaurantOwnerResponse.builder().ownerId(newRestaurantOwner.getId()).name(newRestaurantOwner.getName())
					.mobileNumber(newRestaurantOwner.getMobileNumber()).email(newRestaurantOwner.getEmail()).build();

		} catch (ServiceException e) {
			log.error("Error while registering restaurant owner: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Error while registering restaurant owner: {}", e.getMessage());
			throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
		}
	}

	private Boolean isValidRestaurantOwner(Long restaurantOwnerId, Long restaurantId) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
		if(restaurant == null) {
			log.error("Restaurant with ID {} not found", restaurantId);
			throw new ServiceException(HttpStatus.NOT_FOUND, "Restaurant with ID " + restaurantId + " not found");
		}
		return restaurant.getOwner().getId().equals(restaurantOwnerId);
	}

	public List<RestaurantMenu> searchMenus(String query, Optional<String> type) {
		// Search for menus based on the query
		List<RestaurantMenu> menus = restaurantMenuRepository.findByItemName(query);

		// Filter by type if present
		if (type.isPresent()) {
			menus = menus.stream()
					.filter(menu -> menu.getItemDescription().contains(type.get()))
					.collect(Collectors.toList());
		}

		return menus;
	}
}
