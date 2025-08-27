package com.demo.python_demo.config;

import org.springframework.context.annotation.Configuration;

/**
 * 课程配置类
 * 统一管理课程相关的配置和状态
 */
@Configuration
public class CourseConfig {
    
    /**
     * 课程状态常量
     */
    public static final class Status {
        public static final String PENDING = "pending";      // 待审核
        public static final String APPROVED = "approved";    // 已通过
        public static final String REJECTED = "rejected";    // 已拒绝
        public static final String ACTIVE = "active";        // 已激活
        public static final String DRAFT = "draft";          // 草稿
        public static final String INACTIVE = "inactive";    // 已禁用
    }
    
    /**
     * 课程难度常量
     */
    public static final class Difficulty {
        public static final String BEGINNER = "beginner";           // 初级
        public static final String INTERMEDIATE = "intermediate";   // 中级
        public static final String ADVANCED = "advanced";           // 高级
    }
    
    /**
     * 课程分类常量
     */
    public static final class Category {
        public static final String PROGRAMMING = "编程开发";
        public static final String WEB_DEVELOPMENT = "Web开发";
        public static final String DATA_SCIENCE = "数据科学";
        public static final String ARTIFICIAL_INTELLIGENCE = "人工智能";
        public static final String MOBILE_DEVELOPMENT = "移动开发";
        public static final String GAME_DEVELOPMENT = "游戏开发";
        public static final String CYBERSECURITY = "网络安全";
        public static final String CLOUD_COMPUTING = "云计算";
        public static final String OTHER = "其他";
    }
    
    /**
     * 审核操作常量
     */
    public static final class ReviewAction {
        public static final String APPROVE = "approve";     // 通过
        public static final String REJECT = "reject";       // 拒绝
    }
    
    /**
     * 用户角色/专业常量
     * 1-6: 各专业学生（具体专业类别）
     * 7: 教师
     * 8: 管理员
     */
    public static final class UserRole {
        public static final int COMPUTER_SCIENCE = 1;      // 计算机类
        public static final int INDUSTRIAL_DESIGN = 2;     // 工设类
        public static final int ARTS = 3;                  // 艺术类
        public static final int MEDICINE = 4;              // 医学类
        public static final int LIBERAL_ARTS = 5;          // 文科类
        public static final int SPORTS = 6;                // 体育类
        public static final int TEACHER = 7;               // 教师
        public static final int ADMIN = 8;                 // 管理员
    }
    
    /**
     * 默认值配置
     */
    public static final class Defaults {
        public static final int DEFAULT_VIEWS = 0;
        public static final double DEFAULT_RATING = 0.0;
        public static final int DEFAULT_LESSONS = 0;
        public static final String DEFAULT_DIFFICULTY = Difficulty.BEGINNER;
        public static final String DEFAULT_STATUS = Status.PENDING;
    }
    
    /**
     * 验证课程状态是否有效
     */
    public static boolean isValidStatus(String status) {
        return Status.PENDING.equals(status) ||
               Status.APPROVED.equals(status) ||
               Status.REJECTED.equals(status) ||
               Status.ACTIVE.equals(status) ||
               Status.DRAFT.equals(status) ||
               Status.INACTIVE.equals(status);
    }
    
    /**
     * 验证课程难度是否有效
     */
    public static boolean isValidDifficulty(String difficulty) {
        return Difficulty.BEGINNER.equals(difficulty) ||
               Difficulty.INTERMEDIATE.equals(difficulty) ||
               Difficulty.ADVANCED.equals(difficulty);
    }
    
    /**
     * 验证用户角色是否有效
     */
    public static boolean isValidUserRole(Integer role) {
        return role != null && role >= 1 && role <= 8;
    }
    
    /**
     * 获取状态的中文描述
     */
    public static String getStatusText(String status) {
        switch (status) {
            case Status.PENDING: return "待审核";
            case Status.APPROVED: return "已通过";
            case Status.REJECTED: return "已拒绝";
            case Status.ACTIVE: return "已激活";
            case Status.DRAFT: return "草稿";
            case Status.INACTIVE: return "已禁用";
            default: return "未知状态";
        }
    }
    
    /**
     * 获取难度的中文描述
     */
    public static String getDifficultyText(String difficulty) {
        switch (difficulty) {
            case Difficulty.BEGINNER: return "初级";
            case Difficulty.INTERMEDIATE: return "中级";
            case Difficulty.ADVANCED: return "高级";
            default: return "未知难度";
        }
    }
    
    /**
     * 获取用户角色的中文描述
     */
    public static String getUserRoleText(Integer role) {
        if (role == null) return "未知";
        
        switch (role) {
            case UserRole.COMPUTER_SCIENCE: return "计算机类";
            case UserRole.INDUSTRIAL_DESIGN: return "工设类";
            case UserRole.ARTS: return "艺术类";
            case UserRole.MEDICINE: return "医学类";
            case UserRole.LIBERAL_ARTS: return "文科类";
            case UserRole.SPORTS: return "体育类";
            case UserRole.TEACHER: return "教师";
            case UserRole.ADMIN: return "管理员";
            default: return "未知角色";
        }
    }
    
    /**
     * 判断是否为学生角色
     */
    public static boolean isStudent(Integer role) {
        return role != null && role >= 1 && role <= 6;
    }
    
    /**
     * 判断是否为教师角色
     */
    public static boolean isTeacher(Integer role) {
        return UserRole.TEACHER == role;
    }
    
    /**
     * 判断是否为管理员角色
     */
    public static boolean isAdmin(Integer role) {
        return UserRole.ADMIN == role;
    }
}
