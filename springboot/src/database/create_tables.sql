-- 创建数据库
CREATE DATABASE IF NOT EXISTS `agricultural_product_system` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '农产品系统数据库';

USE `agricultural_product_system`;

-- 用户表
CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
  `role` ENUM('SUPER_ADMIN', 'ADMIN', 'FARMER', 'USER') NOT NULL DEFAULT 'USER' COMMENT '用户角色',
  `email` VARCHAR(50) NOT NULL COMMENT '电子邮箱',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '账号状态(0禁用,1启用)',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 地址表
CREATE TABLE `address` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '地址ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
  `address` VARCHAR(255) NOT NULL COMMENT '详细地址',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收货地址表';

-- 商品表
CREATE TABLE `product` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '商品ID',
  `name` VARCHAR(100) NOT NULL COMMENT '商品名称',
  `description` LONGTEXT COMMENT '商品描述',
  `price` DECIMAL(10,2) NOT NULL COMMENT '商品价格',
  `stock` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
  `category_id` BIGINT NOT NULL COMMENT '分类ID',
  `image_url` VARCHAR(255) COMMENT '商品图片URL',
  `sales_count` INT NOT NULL DEFAULT 0 COMMENT '销量',
  `farmer_id` BIGINT NOT NULL COMMENT '农户ID',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '商品状态:0下架,1上架',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`farmer_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品信息表';

-- 类别表
CREATE TABLE `category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `description` TEXT COMMENT '分类描述',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 订单表
CREATE TABLE `order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `total_price` DECIMAL(10,2) NOT NULL COMMENT '订单总价',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态:0待支付,1已支付,2已发货,3已完成,4已取消,5退款中,6已退款,7退款失败',
  `last_status` TINYINT NOT NULL DEFAULT 0 COMMENT '上一个订单状态',
  `remark` VARCHAR(255) COMMENT '订单备注',
  `refund_time` TIMESTAMP COMMENT '退款时间',
  `refund_status` TINYINT DEFAULT 0 COMMENT '退款状态:0无退款,1申请退款,2退款中,3已退款,4退款失败',
  `refund_reason` VARCHAR(255) COMMENT '退款原因',
  `product_id` BIGINT NOT NULL COMMENT '商品ID',
  `quantity` INT NOT NULL COMMENT '购买数量',
  `price` DECIMAL(10,2) NOT NULL COMMENT '商品单价',
  `farmer_id` BIGINT COMMENT '农户ID',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  FOREIGN KEY (`product_id`) REFERENCES `product`(`id`),
  FOREIGN KEY (`farmer_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 购物车表
CREATE TABLE `cart` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '购物车ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `product_id` BIGINT NOT NULL COMMENT '商品ID',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '商品数量',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  FOREIGN KEY (`product_id`) REFERENCES `product`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 物流表
CREATE TABLE `logistics` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '物流ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `address_id` BIGINT NOT NULL COMMENT '收货地址ID',
  `company` BIGINT NOT NULL COMMENT '物流公司ID',
  `tracking_number` VARCHAR(50) NOT NULL COMMENT '物流单号',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '物流状态:0待发货,1已发货,2已签收,3已取消',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`order_id`) REFERENCES `order`(`id`),
  FOREIGN KEY (`address_id`) REFERENCES `address`(`id`),
  FOREIGN KEY (`company_id`) REFERENCES `company`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流信息表';

-- 评价表
CREATE TABLE `review` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '评价ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `product_id` BIGINT NOT NULL COMMENT '商品ID',
  `rating` TINYINT NOT NULL CHECK (rating BETWEEN 1 AND 5) COMMENT '评分(1-5星)',
  `content` TEXT COMMENT '评价内容',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '评价状态:0待审核,1已通过,2已拒绝',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  FOREIGN KEY (`product_id`) REFERENCES `product`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品评价表';

-- 收藏表
CREATE TABLE `favorite` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '收藏ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `product_id` BIGINT NOT NULL COMMENT '商品ID',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '收藏状态:0取消,1收藏',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  FOREIGN KEY (`product_id`) REFERENCES `product`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品收藏表';

-- 用户行为表
CREATE TABLE `user_behavior` (
  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '行为ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `product_id` BIGINT NOT NULL COMMENT '商品ID',
  `behavior_type` ENUM('浏览', '购买', '收藏') NOT NULL COMMENT '行为类型',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  FOREIGN KEY (`product_id`) REFERENCES `product`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户行为记录表';

-- 创建索引
CREATE INDEX idx_user_username ON `user`(`username`) COMMENT '用户名索引';
CREATE INDEX idx_product_name ON `product`(`name`) COMMENT '商品名称索引';
CREATE INDEX idx_order_user ON `order`