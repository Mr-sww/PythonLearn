-- 使用数据库
USE pythonlearn;

-- 删除已存在的表（如果存在）
DROP TABLE IF EXISTS `video_watch_record`;
DROP TABLE IF EXISTS `knowledge_study_record`;

-- 知识点学习记录表
CREATE TABLE IF NOT EXISTS `knowledge_study_record` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `knowledge_id` int NOT NULL COMMENT '知识点ID',
  `knowledge_title` varchar(255) NOT NULL COMMENT '知识点标题',
  `study_time` int DEFAULT 0 COMMENT '学习时长(秒)',
  `progress` decimal(5,2) DEFAULT 0.00 COMMENT '学习进度百分比',
  `status` enum('started','in_progress','completed') DEFAULT 'started' COMMENT '学习状态',
  `start_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '开始学习时间',
  `end_time` datetime NULL COMMENT '结束学习时间',
  `last_study_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后学习时间',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_knowledge` (`user_id`, `knowledge_id`),
  KEY `idx_user_status` (`user_id`, `status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识点学习记录表';

-- 视频观看记录表
CREATE TABLE IF NOT EXISTS `video_watch_record` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `video_id` int NOT NULL COMMENT '视频ID',
  `video_title` varchar(255) NOT NULL COMMENT '视频标题',
  `video_url` varchar(500) NOT NULL COMMENT '视频URL',
  `total_duration` int DEFAULT 0 COMMENT '视频总时长(秒)',
  `watch_time` int DEFAULT 0 COMMENT '观看时长(秒)',
  `progress` decimal(5,2) DEFAULT 0.00 COMMENT '观看进度百分比',
  `status` enum('started','in_progress','completed') DEFAULT 'started' COMMENT '观看状态',
  `start_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '开始观看时间',
  `end_time` datetime NULL COMMENT '结束观看时间',
  `last_watch_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后观看时间',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_video` (`user_id`, `video_id`),
  KEY `idx_user_status` (`user_id`, `status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='视频观看记录表';

-- 显示创建的表
SHOW TABLES LIKE '%record%';

-- 显示表结构
DESCRIBE knowledge_study_record;
DESCRIBE video_watch_record;









