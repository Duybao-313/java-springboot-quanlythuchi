-- V1__create_tables.sql
CREATE TABLE `role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_role_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `address` VARCHAR(255) DEFAULT NULL,
  `avatar_url` VARCHAR(255) DEFAULT NULL,
  `created_at` DATETIME(6) DEFAULT NULL,
  `email` VARCHAR(255) NOT NULL,
  `full_name` VARCHAR(255) DEFAULT NULL,
  `password` VARCHAR(255) NOT NULL,
  `phone` VARCHAR(255) DEFAULT NULL,
  `status` ENUM('ACTIVE','DELETED','INACTIVE','PENDING','SUSPENDED') DEFAULT NULL,
  `updated_at` DATETIME(6) DEFAULT NULL,
  `username` VARCHAR(255) NOT NULL,
  `role_id` INT DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_users_email` (`email`),
  UNIQUE KEY `UK_users_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `categories` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `color` VARCHAR(255) DEFAULT NULL,
  `icon_url` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) NOT NULL,
  `type` ENUM('EXPENSE','INCOME') NOT NULL,
  `owner_id` BIGINT DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_categories_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `wallets` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `balance` DECIMAL(38,2) NOT NULL,
  `created_at` DATETIME(6) DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `icon_url` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) NOT NULL,
  `type` ENUM('BANK','CASH','E_WALLET') NOT NULL,
  `updated_at` DATETIME(6) DEFAULT NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `transactions` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `amount` DECIMAL(38,2) NOT NULL,
  `date` DATETIME(6) NOT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `type` ENUM('EXPENSE','INCOME') DEFAULT NULL,
  `category_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `wallet_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `budget` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `amount_limit` DECIMAL(38,2) DEFAULT NULL,
  `end_date` DATE DEFAULT NULL,
  `start_date` DATE DEFAULT NULL,
  `category_id` BIGINT DEFAULT NULL,
  `user_id` BIGINT DEFAULT NULL,
  `amount` DECIMAL(18,2) NOT NULL,
  `created_at` DATETIME(6) DEFAULT NULL,
  `name` VARCHAR(255) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `period_type` TINYINT DEFAULT NULL,
  `status` TINYINT DEFAULT NULL,
  `updated_at` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `budget_history` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `change_type` TINYINT DEFAULT NULL,
  `changed_by` BIGINT DEFAULT NULL,
  `created_at` DATETIME(6) DEFAULT NULL,
  `new_value_json` TEXT DEFAULT NULL,
  `note` VARCHAR(255) DEFAULT NULL,
  `old_value_json` TEXT DEFAULT NULL,
  `budget_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `budget_scope` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `ref_id` BIGINT NOT NULL,
  `scope_type` TINYINT NOT NULL,
  `budget_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `budget_threshold` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `action` TINYINT NOT NULL,
  `created_at` DATETIME(6) DEFAULT NULL,
  `percent` INT NOT NULL,
  `budget_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `user_category` (
  `user_id` BIGINT NOT NULL,
  `category_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_id`,`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `invalidated_token` (
  `id` VARCHAR(255) NOT NULL,
  `expiry_time` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;