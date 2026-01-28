-- V2__add_foreign_keys.sql
ALTER TABLE `budget`
  ADD KEY `idx_budget_category` (`category_id`),
  ADD KEY `idx_budget_user` (`user_id`);

ALTER TABLE `budget_history`
  ADD KEY `idx_history_budget` (`budget_id`);

ALTER TABLE `budget_scope`
  ADD KEY `idx_scope_budget` (`budget_id`);

ALTER TABLE `budget_threshold`
  ADD KEY `idx_threshold_budget` (`budget_id`);

ALTER TABLE `categories`
  ADD KEY `idx_categories_owner` (`owner_id`);

ALTER TABLE `transactions`
  ADD KEY `idx_transactions_category` (`category_id`),
  ADD KEY `idx_transactions_user` (`user_id`),
  ADD KEY `idx_transactions_wallet` (`wallet_id`);

ALTER TABLE `users`
  ADD KEY `idx_users_role` (`role_id`);

ALTER TABLE `user_category`
  ADD KEY `idx_user_category_category` (`category_id`);

ALTER TABLE `wallets`
  ADD KEY `idx_wallets_user` (`user_id`);

ALTER TABLE `budget`
  ADD CONSTRAINT `FK_budget_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FK_budget_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);

ALTER TABLE `budget_history`
  ADD CONSTRAINT `FK_history_budget` FOREIGN KEY (`budget_id`) REFERENCES `budget` (`id`);

ALTER TABLE `budget_scope`
  ADD CONSTRAINT `FK_scope_budget` FOREIGN KEY (`budget_id`) REFERENCES `budget` (`id`);

ALTER TABLE `budget_threshold`
  ADD CONSTRAINT `FK_threshold_budget` FOREIGN KEY (`budget_id`) REFERENCES `budget` (`id`);

ALTER TABLE `categories`
  ADD CONSTRAINT `FK_categories_owner` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`);

ALTER TABLE `transactions`
  ADD CONSTRAINT `FK_transactions_wallet` FOREIGN KEY (`wallet_id`) REFERENCES `wallets` (`id`),
  ADD CONSTRAINT `FK_transactions_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FK_transactions_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);

ALTER TABLE `users`
  ADD CONSTRAINT `FK_users_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`);

ALTER TABLE `user_category`
  ADD CONSTRAINT `FK_user_category_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FK_user_category_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);

ALTER TABLE `wallets`
  ADD CONSTRAINT `FK_wallets_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);