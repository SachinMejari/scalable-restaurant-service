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
@Table(name = "restaurant_owners")
public class RestaurantOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name="mobile_number", nullable = false, length = 10)
    private String mobileNumber;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(name="is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name="is_archived", nullable = false)
    private Boolean isArchived = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
