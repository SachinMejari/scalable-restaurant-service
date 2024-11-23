CREATE TABLE restaurant_owners (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    mobile_number VARCHAR(10) NOT NULL,
    email VARCHAR(100) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    is_archived BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);

CREATE TABLE restaurants (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    contact_no VARCHAR(15) NOT NULL,
    opening_days VARCHAR(100) NOT NULL,
    opening_time VARCHAR(25) NOT NULL,
    closing_time VARCHAR(25) NOT NULL,
    dine_in BOOLEAN NOT NULL DEFAULT false,
    take_away BOOLEAN NOT NULL DEFAULT false,
    owner_id BIGINT NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    is_archived BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_restaurants_owner_id` FOREIGN KEY (`owner_id`) REFERENCES `restaurant_owners` (`id`)
);

CREATE TABLE restaurant_types (
    id BIGINT AUTO_INCREMENT,
    restaurant_id BIGINT NOT NULL,
    cuisine VARCHAR(100) NOT NULL,
    veg BOOLEAN NOT NULL DEFAULT false,
    non_veg BOOLEAN NOT NULL DEFAULT false,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    is_archived BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_restaurant_types_restaurant_id` (`restaurant_id`),
    CONSTRAINT `fk_restaurant_types_restaurant_id` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`id`)
);

CREATE TABLE restaurant_menus (
    id BIGINT AUTO_INCREMENT,
    restaurant_id BIGINT NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    item_description VARCHAR(255) NOT NULL,
    item_price DECIMAL(10,2) NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT true,
    is_deleted BOOLEAN NOT NULL DEFAULT false,
    is_archived BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_restaurant_menus_restaurant_id` (`restaurant_id`),
    CONSTRAINT `fk_restaurant_menus_restaurant_id` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurants` (`id`)
);