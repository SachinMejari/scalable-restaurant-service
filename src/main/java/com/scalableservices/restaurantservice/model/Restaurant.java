package com.scalableservices.restaurantservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(name = "contact_no",nullable = false, length = 15)
    private String contactNo;

    @Column(name = "opening_days",nullable = false, length = 100)
    private String openingDays;

    @Column(name = "opening_time",nullable = false)
    private String openingTime;

    @Column(name = "closing_time", nullable = false)
    private String closingTime;

    @Column(name = "dine_in",nullable = false)
    private Boolean dineIn = false;

    @Column(name = "take_away",nullable = false)
    private Boolean takeAway = false;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false, foreignKey = @ForeignKey(name = "fk_restaurants_owner_id"))
    private RestaurantOwner owner;

    @Column(name="is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name="is_archived", nullable = false)
    private Boolean isArchived = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}