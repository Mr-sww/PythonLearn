package com.demo.python_demo.controller;

import com.demo.python_demo.entity.User;
import com.demo.python_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import com.demo.python_demo.service.UserProblemRecordService;
import com.demo.python_demo.repository.PythonProblemRepository;
import com.demo.python_demo.service.LearningProgressService;

import java.util.HashMap;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserProblemRecordService userProblemRecordService;

    @Autowired
    private PythonProblemRepository pythonProblemRepository;

    @Autowired
    private LearningProgressService learningProgressService;

    /**
     * 获取所有用户
     * @return 用户列表
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * 根据ID获取用户
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.getUserById(id).orElse(null);
    }

    /**
     * 创建用户
     * @param user 用户信息
     * @return 创建的用户
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        boolean success = userService.register(user);
        if (success) {
            User newUser = userService.getUserByAccount(user.getAccount()).orElse(null);
            return ResponseEntity.ok(newUser);
        }
        return ResponseEntity.status(400).body("账号或手机号已存在");
    }

    /**
     * 更新用户信息
     * @param id 用户ID
     * @param params 用户信息参数
     * @return 更新后的用户
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody Map<String, Object> params) {
        User user = userService.getUserById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("用户不存在");
        }
        // 昵称
        if (params.containsKey("nickname")) {
            user.setNickname((String) params.get("nickname"));
        }
        // 头像
        if (params.containsKey("avatar")) {
            user.setAvatar((String) params.get("avatar"));
        }
        // 专业大类
        if (params.containsKey("groupType")) {
            Object groupTypeObj = params.get("groupType");
            if (groupTypeObj != null) {
                user.setGroupType(Integer.parseInt(groupTypeObj.toString()));
            }
        }
        // 兴趣方向（支持数组或字符串）
        if (params.containsKey("intestTypes")) {
            Object intestTypesObj = params.get("intestTypes");
            if (intestTypesObj instanceof List) {
                List<?> list = (List<?>) intestTypesObj;
                user.setIntestTypes(list.stream().map(String::valueOf).collect(Collectors.joining(",")));
            } else if (intestTypesObj != null) {
                user.setIntestTypes(intestTypesObj.toString());
            }
        }
        // 密码（如有修改，需加密）
        if (params.containsKey("password")) {
            String pwd = (String) params.get("password");
            if (pwd != null && !pwd.isEmpty()) {
                user.setPassword(passwordEncoder.encode(pwd));
            }
        }
        // 邮箱
        if (params.containsKey("email")) {
            user.setEmail((String) params.get("email"));
        }
        userService.updateUser(user);
        return ResponseEntity.ok(user);
    }

    /**
     * 删除用户
     * @param userId 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 用户登录
     * @param params 登录参数
     * @return 登录后的用户
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> params, HttpSession session) {
        String account = params.get("account");
        String password = params.get("password");
        User user = userService.login(account, password);
        if (user != null) {
            session.setAttribute("user", user);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(400).body("账号或密码错误");
    }

    /**
     * 获取用户学习统计
     * @param userId 用户ID
     * @return 学习统计信息
     */
    @GetMapping("/{userId}/statistics")
    public ResponseEntity<?> getLearningStatistics(@PathVariable Integer userId) {
        return userService.getUserById(userId)
                .map(user -> ResponseEntity.ok(userService.getLearningStatistics(userId)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 只更新用户的 groupType 字段
     */
    @PatchMapping("/{id}/groupType")
    public ResponseEntity<?> updateGroupType(@PathVariable Integer id, @RequestBody Map<String, Integer> params) {
        Integer groupType = params.get("groupType");
        int result = userService.updateGroupType(id, groupType);
        if (result > 0) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(400).body("更新失败");
    }

    /**
     * 更新用户的兴趣方向
     * @param id 用户ID
     * @param params 兴趣方向参数
     * @return 更新结果
     */
    @PatchMapping("/{id}/intestTypes")
    public ResponseEntity<?> updateIntestTypes(@PathVariable Integer id, @RequestBody Map<String, String> params) {
        String intestTypes = params.get("intestTypes");
        int result = userService.updateIntestTypes(id, intestTypes);
        if (result > 0) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(400).body("更新失败");
    }

    /**
     * 上传用户头像
     * @param id 用户ID
     * @param file 头像文件
     * @return 头像URL
     */
    @PostMapping("/{id}/avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable Integer id, @RequestParam("avatar") MultipartFile file) {
        try {
            // 1. 校验文件类型和大小
            String contentType = file.getContentType();
            if (file.getSize() > 2 * 1024 * 1024) { // 2MB
                return ResponseEntity.status(400).body("图片不能超过2MB");
            }
            if (!("image/jpeg".equals(contentType) || "image/png".equals(contentType))) {
                return ResponseEntity.status(400).body("只支持jpg/png格式");
            }

            // 2. 保存文件到项目根目录 avatar 文件夹
            String ext = contentType.equals("image/png") ? ".png" : ".jpg";
            String fileName = "avatar_" + id + "_" + System.currentTimeMillis() + ext;
            String basePath = System.getProperty("user.dir") + java.io.File.separator + "avatar";
            java.io.File dir = new java.io.File(basePath);
            if (!dir.exists()) dir.mkdirs();
            java.io.File dest = new java.io.File(dir, fileName);
            file.transferTo(dest);

            // 3. 生成可访问的URL（/avatar/xxx.jpg）
            String url = "/avatar/" + fileName;

            // 4. 更新数据库
            User user = userService.getUserById(id).orElse(null);
            if (user != null) {
                user.setAvatar(url);
                userService.updateUser(user);
            }

            // 5. 返回
            return ResponseEntity.ok(java.util.Collections.singletonMap("avatar", url));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("上传失败");
        }
    }

    /**
     * 获取当前登录用户信息
     * @param session 当前会话
     * @return 当前登录用户信息
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(401).body("未登录");
    }

    /**
     * 用户注销（退出登录）
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate(); // 清除 session
        // 手动清除 JSESSIONID cookie
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取当前用户做题统计信息（动态卡片数据）
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getUserStatistics(HttpSession session) {
        User user = (User) session.getAttribute("user");
        int totalProblems = pythonProblemRepository.findAll().size();
        if (user == null) {
            // 未登录
            return ResponseEntity.ok(Map.of(
                "completed", 0,
                "accuracy", 0,
                "practiceTime", 0,
                "totalProblems", totalProblems,
                "continuousDays", 0
            ));
        }
        Integer userId = user.getUserId();
        // 统计做题数（去重）、正确率、练习时长
        int completed = userProblemRecordService.getPassedProblems(userId); // 已完成题数
        double accuracy = userProblemRecordService.getAccuracy(userId) * 100; // 百分比
        int practiceTime = userProblemRecordService.getUserStatistics(userId).getOrDefault("sumUsedTime", 0) instanceof Integer ?
            (Integer)userProblemRecordService.getUserStatistics(userId).getOrDefault("sumUsedTime", 0) : 0;
        int continuousDays = userProblemRecordService.getContinuousDays(userId);
        // 若无记录则插入一条默认记录（service已实现）
        return ResponseEntity.ok(Map.of(
            "completed", completed,
            "accuracy", (int)accuracy,
            "practiceTime", practiceTime,
            "totalProblems", totalProblems,
            "continuousDays", continuousDays
        ));
    }

    /**
     * 获取当前用户学习统计信息（学习中心卡片数据）
     */
    @GetMapping("/learning-statistics")
    public ResponseEntity<?> getLearningStatistics(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.ok(Map.of(
                "totalStudyHours", 0,
                "completedCourses", 0,
                "continuousDays", 0,
                "login", false
            ));
        }
        Integer userId = user.getUserId();
        // 统计总学习时长、已完成课程数
        Object statsObj = learningProgressService.getUserStatistics(userId);
        int totalStudySeconds = 0;
        int completedCourses = 0;
        if (statsObj instanceof Map) {
            Map map = (Map) statsObj;
            totalStudySeconds = map.getOrDefault("totalTimeSpent", 0) instanceof Integer ? (Integer) map.getOrDefault("totalTimeSpent", 0) : 0;
            completedCourses = map.getOrDefault("completedCourses", 0) instanceof Integer ? (Integer) map.getOrDefault("completedCourses", 0) : 0;
        }
        double totalStudyHours = Math.round((totalStudySeconds / 3600.0) * 10) / 10.0;
        // 连续学习天数（可根据学习进度表的lastStudyTime实现，这里简单返回0或1，后续可扩展）
        int continuousDays = 0;
        // TODO: 可扩展为真实连续天数统计
        return ResponseEntity.ok(Map.of(
            "totalStudyHours", totalStudyHours,
            "completedCourses", completedCourses,
            "continuousDays", continuousDays,
            "login", true
        ));
    }

    @GetMapping("/learning-progress")
    public Map<String, Object> getLearningProgress() {
        Map<String, Object> progress = new HashMap<>();
        progress.put("progress", 80);
        progress.put("days", 12);
        progress.put("coursesCompleted", 5);
        return progress;
    }
}
