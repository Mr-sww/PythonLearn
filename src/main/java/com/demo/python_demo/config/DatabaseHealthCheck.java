package com.demo.python_demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库健康检查配置
 * 帮助诊断数据库连接和表结构问题
 */
@Component
@RestController
@RequestMapping("/api/admin/health")
public class DatabaseHealthCheck {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取数据库健康状态
     */
    @GetMapping("/database")
    public Map<String, Object> getDatabaseHealth() {
        Map<String, Object> health = new HashMap<>();
        
        try {
            // 检查数据库连接
            try (Connection connection = dataSource.getConnection()) {
                DatabaseMetaData metaData = connection.getMetaData();
                
                health.put("status", "UP");
                health.put("database", metaData.getDatabaseProductName());
                health.put("version", metaData.getDatabaseProductVersion());
                health.put("url", metaData.getURL());
                health.put("user", metaData.getUserName());
                
                // 检查关键表是否存在
                boolean userTableExists = checkTableExists("user");
                health.put("user_table", userTableExists ? "OK" : "MISSING");
                
                boolean courseTableExists = checkTableExists("course");
                health.put("course_table", courseTableExists ? "OK" : "MISSING");
                
                // 检查课程表结构
                if (courseTableExists) {
                    Map<String, String> courseColumns = checkCourseTableStructure();
                    health.put("course_columns", courseColumns);
                }
                
                // 检查数据量
                if (userTableExists) {
                    try {
                        int userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user", Integer.class);
                        health.put("user_count", userCount);
                    } catch (Exception e) {
                        health.put("user_count_error", e.getMessage());
                    }
                }
                
                if (courseTableExists) {
                    try {
                        int courseCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM course", Integer.class);
                        health.put("course_count", courseCount);
                        
                        // 检查课程状态分布
                        Map<String, Integer> statusDistribution = checkCourseStatusDistribution();
                        health.put("course_status_distribution", statusDistribution);
                    } catch (Exception e) {
                        health.put("course_count_error", e.getMessage());
                    }
                }
                
            }
        } catch (Exception e) {
            health.put("status", "DOWN");
            health.put("error", e.getMessage());
        }
        
        return health;
    }

    /**
     * 检查表是否存在
     */
    private boolean checkTableExists(String tableName) {
        try {
            String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'pythonlearn' AND table_name = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查课程表结构
     */
    private Map<String, String> checkCourseTableStructure() {
        Map<String, String> columns = new HashMap<>();
        try {
            String sql = "DESCRIBE course";
            jdbcTemplate.query(sql, (rs, rowNum) -> {
                String columnName = rs.getString("Field");
                String columnType = rs.getString("Type");
                columns.put(columnName, columnType);
                return null;
            });
        } catch (Exception e) {
            columns.put("error", e.getMessage());
        }
        return columns;
    }

    /**
     * 检查课程状态分布
     */
    private Map<String, Integer> checkCourseStatusDistribution() {
        Map<String, Integer> distribution = new HashMap<>();
        try {
            String sql = "SELECT Status, COUNT(*) as count FROM course GROUP BY Status";
            jdbcTemplate.query(sql, (rs, rowNum) -> {
                String status = rs.getString("Status");
                int count = rs.getInt("count");
                distribution.put(status, count);
                return null;
            });
        } catch (Exception e) {
            distribution.put("error", -1);
        }
        return distribution;
    }

    /**
     * 获取详细的健康信息（用于调试）
     */
    @GetMapping("/detailed")
    public Map<String, Object> getDetailedHealthInfo() {
        return getDatabaseHealth();
    }
}
