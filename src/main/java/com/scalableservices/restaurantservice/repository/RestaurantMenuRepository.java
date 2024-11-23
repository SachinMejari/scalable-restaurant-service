package com.scalableservices.restaurantservice.repository;

import com.scalableservices.restaurantservice.model.RestaurantMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, Long> {
    
    // Find all menu items by restaurant ID
    List<RestaurantMenu> findByRestaurantId(Long restaurantId);
    
    // Find all available menu items by restaurant ID
    List<RestaurantMenu> findByRestaurantIdAndIsAvailableTrue(Long restaurantId);
    
    // Find menu item by name for a specific restaurant
    RestaurantMenu findByRestaurantIdAndItemName(Long restaurantId, String itemName);
    List<RestaurantMenu> findByItemName(String name);
}
