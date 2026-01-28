-- V3__seed_roles.sql
-- Thêm dữ liệu seed cho bảng role
-- Dùng INSERT ... ON DUPLICATE KEY UPDATE để chạy idempotent (chạy nhiều lần không tạo duplicate)

INSERT INTO `role` (`id`, `name`)
VALUES
  (1, 'ROLE_USER'),
  (2, 'ROLE_ADMIN')
ON DUPLICATE KEY UPDATE
  `name` = VALUES(`name`);