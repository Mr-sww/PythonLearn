-- 知识点评论表（运行于数据库 pythonlearn 或你当前使用的库）
-- 执行前：USE your_database_name;

CREATE TABLE IF NOT EXISTS `knowledge_comment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `knowledge_id` INT NOT NULL COMMENT '对应 runoobpython3install.id',
  `user_id` INT DEFAULT 0 COMMENT '留言用户ID，可为0表示匿名',
  `nickname` VARCHAR(100) DEFAULT '' COMMENT '冗余昵称，便于展示',
  `avatar` VARCHAR(255) DEFAULT '' COMMENT '用户头像URL（冗余）',
  `parent_id` INT NOT NULL DEFAULT 0 COMMENT '父评论ID，0为顶级',
  `root_id` INT NOT NULL DEFAULT 0 COMMENT '根评论ID，0为顶级',
  `reply_to_user_id` INT NOT NULL DEFAULT 0 COMMENT '被回复用户ID',
  `content` TEXT NOT NULL,
  `likes` BIGINT NOT NULL DEFAULT 0 COMMENT '点赞数，支持大数量',
  `reply_count` BIGINT NOT NULL DEFAULT 0 COMMENT '子回复数量',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_knowledge_id` (`knowledge_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


