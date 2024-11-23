package com.scalableservices.restaurantservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restaurant_menus")
public class RestaurantMenu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "restaurant_id", nullable = false, foreignKey = @ForeignKey(name = "fk_restaurant_menus_restaurant_id"))
	private Restaurant restaurant;

	@Column(name = "item_name", nullable = false, length = 100)
	private String itemName;

	@Column(name = "item_description", nullable = false, length = 255)
	private String itemDescription;

	@Column(name = "item_price", nullable = false, precision = 10, scale = 2)
	private BigDecimal itemPrice;

	@Column(name = "is_available", nullable = false)
	private Boolean isAvailable = true;

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted = false;

	@Column(name = "is_archived", nullable = false)
	private Boolean isArchived = false;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "updated_at")
	private LocalDateTime updatedAt = LocalDateTime.now();

	// Getters and setters
}