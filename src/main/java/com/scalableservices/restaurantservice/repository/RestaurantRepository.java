package com.scalableservices.restaurantservice.repository;

import com.scalableservices.restaurantservice.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findById(Long id);
    Restaurant findByContactNo(String contactNo);
    Restaurant findByContactNoAndIdNot(String contactNo, Long restaurantId);
}
