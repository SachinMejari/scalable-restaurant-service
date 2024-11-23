package com.scalableservices.restaurantservice.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant_types")
public class RestaurantType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false, foreignKey = @ForeignKey(name = "fk_restaurant_types_restaurant_id"))
    private Restaurant restaurant;

    @Column(nullable = false, length = 100)
    private String cuisine;

    @Column(nullable = false)
    private Boolean veg = false;

    @Column(name="non_veg", nullable = false)
    private Boolean nonVeg = false;

    @Column(name="is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name="is_archived", nullable = false)
    private Boolean isArchived = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Getters and setters
}

