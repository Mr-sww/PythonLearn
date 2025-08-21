package com.demo.python_demo.controller;

import com.demo.python_demo.entity.UserProblemRecord;
import com.demo.python_demo.service.UserProblemRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;

/**
 * 代码判题控制器 - 实现真正的代码执行功能
 */
@RestController
@RequestMapping("/api/judge")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class CodeJudgeController {

    @Autowired
    private UserProblemRecordService userProblemRecordService;

    // 代码执行超时时间（秒）
    private static final int EXECUTION_TIMEOUT = 5;
    
    // 临时文件目录
    private static final String TEMP_DIR = "temp_code";

    /**
     * 运行代码
     */
    @PostMapping("/run")
    public ResponseEntity<Map<String, Object>> runCode(@RequestBody Map<String, Object> request) {
        try {
            String code = (String) request.get("code");
            String language = (String) request.get("language");
            String input = (String) request.get("input");

            // 验证输入
            if (code == null || code.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "代码不能为空",
                    "message", "请提供有效的代码"
                ));
            }

            // 代码长度限制
            if (code.length() > 10000) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "代码过长",
                    "message", "代码长度不能超过10000字符"
                ));
            }

            // 执行代码
            Map<String, Object> result = executeCode(code, language, input);
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "执行失败",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * 批量判题
     */
    @PostMapping("/batch-judge")
    public ResponseEntity<List<Map<String, Object>>> batchJudge(@RequestBody Map<String, Object> request) {
        try {
            String code = (String) request.get("code");
            String language = (String) request.get("language");
            Integer userId = (Integer) request.get("userId");
            Integer problemId = (Integer) request.get("problemId");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> testCases = (List<Map<String, Object>>) request.get("testCases");

            // 验证输入
            if (code == null || code.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.emptyList());
            }

            if (testCases == null || testCases.isEmpty()) {
                return ResponseEntity.badRequest().body(Collections.emptyList());
            }

            // 代码长度限制
            if (code.length() > 10000) {
                return ResponseEntity.badRequest().body(Collections.emptyList());
            }

            // 测试用例数量限制
            if (testCases.size() > 20) {
                return ResponseEntity.badRequest().body(Collections.emptyList());
            }

            List<Map<String, Object>> results = new ArrayList<>();
            int passedCount = 0;
            int totalCount = testCases.size();
            
            for (Map<String, Object> testCase : testCases) {
                String input = (String) testCase.get("input");
                String expected = (String) testCase.get("expected");
                
                Map<String, Object> result = executeCode(code, language, input);
                
                // 判断是否通过
                boolean passed = expected != null && expected.equals(result.get("output"));
                if (passed) passedCount++;
                
                Map<String, Object> caseResult = new HashMap<>(result);
                caseResult.put("input", input);
                caseResult.put("expected", expected);
                caseResult.put("passed", passed);
                
                results.add(caseResult);
            }
            
            // 记录提交记录（服务层/仓库已做“同一用户同题同日只记一条”去重）
            if (userId != null && problemId != null) {
                try {
                    UserProblemRecord record = new UserProblemRecord();
                    record.setUserId(userId);
                    record.setProblemId(problemId);
                    record.setCode(code);
                    record.setLanguage(language);
                    
                    // 计算通过率
                    double passRate = totalCount > 0 ? (double) passedCount / totalCount : 0.0;
                    record.setPassRate(passRate);
                    
                    // 设置结果
                    if (passRate == 1.0) {
                        record.setResult("通过");
                    } else if (passRate > 0) {
                        record.setResult("部分通过");
                    } else {
                        record.setResult("未通过");
                    }
                    
                    // 设置执行时间和内存使用
                    if (!results.isEmpty()) {
                        Map<String, Object> firstResult = results.get(0);
                        Object execObj = firstResult.get("executionTime");
                        Object memObj = firstResult.get("memoryUsage");
                        int execMs = execObj instanceof Number ? ((Number) execObj).intValue() : 0;
                        int memKb = memObj instanceof Number ? ((Number) memObj).intValue() : 0;
                        record.setExecutionTime(execMs);
                        record.setMemoryUsage(memKb);
                    }
                    
                    // 设置默认值
                    record.setUsedTime(0);
                    record.setUsedMemory(0);
                    record.setScore(0);
                    
                    userProblemRecordService.saveRecord(record);
                } catch (Exception e) {
                    // 记录失败不影响判题结果
                    System.err.println("保存提交记录失败: " + e.getMessage());
                }
            }
            
            return ResponseEntity.ok(results);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Collections.emptyList());
        }
    }

    /**
     * 执行代码的核心方法
     */
    private Map<String, Object> executeCode(String code, String language, String input) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if ("python".equalsIgnoreCase(language) || "python3".equalsIgnoreCase(language)) {
                return executePythonCode(code, input);
            } else {
                result.put("output", "");
                result.put("stdout", "");
                result.put("stderr", "不支持的语言: " + language);
                result.put("executionTime", 0);
                result.put("memoryUsage", 0);
                return result;
            }
            
        } catch (Exception e) {
            result.put("output", "");
            result.put("stdout", "");
            result.put("stderr", "执行错误: " + e.getMessage());
            result.put("executionTime", 0);
            result.put("memoryUsage", 0);
            return result;
        }
    }

    /**
     * 执行Python代码
     */
    private Map<String, Object> executePythonCode(String code, String input) throws Exception {
        Map<String, Object> result = new HashMap<>();
        long startTime = System.currentTimeMillis();
        
        // 创建临时目录
        Path tempDir = Paths.get(TEMP_DIR);
        if (!Files.exists(tempDir)) {
            Files.createDirectories(tempDir);
        }
        
        // 生成唯一的文件名
        String fileName = "code_" + System.currentTimeMillis() + ".py";
        Path codeFile = tempDir.resolve(fileName);
        
        try {
            // 写入代码到文件
            Files.write(codeFile, code.getBytes("UTF-8"));
            
            // 构建Python命令 - 尝试不同的Python命令
            ProcessBuilder pb = null;
            String pythonCommand = null;
            
            // 尝试找到可用的Python命令
            String[] pythonCommands = {"python", "python3", "py"};
            for (String cmd : pythonCommands) {
                try {
                    Process testProcess = new ProcessBuilder(cmd, "--version").start();
                    if (testProcess.waitFor(2, TimeUnit.SECONDS) && testProcess.exitValue() == 0) {
                        pythonCommand = cmd;
                        break;
                    }
                } catch (Exception e) {
                    // 继续尝试下一个命令
                }
            }
            
            if (pythonCommand == null) {
                throw new RuntimeException("未找到可用的Python解释器。请确保Python已正确安装并添加到系统PATH中。");
            }
            
            // 由于已将工作目录切换到 tempDir，命令参数只需要文件名，避免路径重复
            pb = new ProcessBuilder(pythonCommand, codeFile.getFileName().toString());
            System.out.println("使用Python命令: " + pythonCommand);
            
            // 设置工作目录
            pb.directory(tempDir.toFile());
            
            // 合并错误流到标准输出流
            pb.redirectErrorStream(true);
            
            System.out.println("=== 调试信息 ===");
            System.out.println("代码文件: " + codeFile.toAbsolutePath());
            System.out.println("代码内容: " + code);
            System.out.println("输入内容: " + input);
            
            // 启动进程
            Process process = pb.start();
            
            // 如果有输入，写入到进程
            if (input != null && !input.trim().isEmpty()) {
                try (OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream(), "UTF-8")) {
                    writer.write(input);
                    writer.write("\n"); // 添加换行符
                    writer.flush();
                    System.out.println("已写入输入: " + input);
                }
            }
            
            // 等待进程完成，设置超时
            boolean completed = process.waitFor(EXECUTION_TIMEOUT, TimeUnit.SECONDS);
            
            long executionTime = System.currentTimeMillis() - startTime;
            
            if (!completed) {
                // 超时，强制终止进程
                process.destroyForcibly();
                result.put("output", "");
                result.put("stdout", "");
                result.put("stderr", "执行超时（超过" + EXECUTION_TIMEOUT + "秒）");
                result.put("executionTime", executionTime);
                result.put("memoryUsage", 0);
                return result;
            }
            
            // 读取输出
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            
            String outputStr = output.toString().trim();
            
            System.out.println("进程退出码: " + process.exitValue());
            System.out.println("输出内容: " + outputStr);
            
            result.put("output", outputStr);
            result.put("stdout", outputStr);
            result.put("stderr", null);
            result.put("executionTime", executionTime);
            result.put("memoryUsage", 1024 + new Random().nextInt(512)); // 模拟内存使用
            
        } finally {
            // 清理临时文件
            try {
                Files.deleteIfExists(codeFile);
            } catch (Exception e) {
                // 忽略删除失败的错误
            }
        }
        
        return result;
    }

    /**
     * 获取支持的编程语言
     */
    @GetMapping("/languages")
    public ResponseEntity<List<Map<String, Object>>> getSupportedLanguages() {
        List<Map<String, Object>> languages = Arrays.asList(
            Map.of("id", "python", "name", "Python 3", "version", "3.9", "extension", ".py"),
            Map.of("id", "python3", "name", "Python 3", "version", "3.9", "extension", ".py")
        );
        
        return ResponseEntity.ok(languages);
    }

    /**
     * 获取代码模板
     */
    @GetMapping("/templates/{language}")
    public ResponseEntity<Map<String, Object>> getCodeTemplate(@PathVariable String language) {
        Map<String, String> templates = Map.of(
            "python", "# Python代码模板\n\ndef main():\n    # 在这里编写你的代码\n    pass\n\nif __name__ == \"__main__\":\n    main()",
            "python3", "# Python代码模板\n\ndef main():\n    # 在这里编写你的代码\n    pass\n\nif __name__ == \"__main__\":\n    main()"
        );
        
        String template = templates.getOrDefault(language, "// 代码模板");
        
        return ResponseEntity.ok(Map.of(
            "language", language,
            "template", template
        ));
    }

    @PostMapping("/run-legacy")
    public Map<String, Object> runCodeLegacy(@RequestBody Map<String, Object> payload) {
        String code = (String) payload.get("code");
        String language = (String) payload.getOrDefault("language", "python");
        String input = (String) payload.getOrDefault("input", "");
        Map<String, Object> ret = new HashMap<>();
        if (!"python".equalsIgnoreCase(language) && !"python3".equalsIgnoreCase(language)) {
            ret.put("status", "error");
            ret.put("stderr", "仅支持Python判题");
            return ret;
        }
        try {
            // 调用本地 judge.py
            ProcessBuilder pb = new ProcessBuilder("python", "judge.py");
            Process process = pb.start();
            // 传递参数
            String json = String.format("{\"code\":%s,\"input\":%s}",
                toJsonString(code), toJsonString(input));
            OutputStream os = process.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            reader.close();
            process.waitFor();
            // 解析Python返回的JSON
            ret = parseJsonToMap(sb.toString());
            ret.putIfAbsent("status", "success");
        } catch (Exception e) {
            ret.put("status", "error");
            ret.put("stderr", e.getMessage());
        }
        return ret;
    }

    @PostMapping("/batch-judge-legacy")
    public List<Map<String, Object>> batchJudgeLegacy(@RequestBody Map<String, Object> payload) {
        String code = (String) payload.get("code");
        String language = (String) payload.getOrDefault("language", "python");
        List<Map<String, String>> testCases = (List<Map<String, String>>) payload.get("testCases");
        List<Map<String, Object>> results = new ArrayList<>();
        if (!"python".equalsIgnoreCase(language) && !"python3".equalsIgnoreCase(language)) {
            Map<String, Object> err = new HashMap<>();
            err.put("status", "error");
            err.put("stderr", "仅支持Python判题");
            results.add(err);
            return results;
        }
        for (Map<String, String> testCase : testCases) {
            String input = testCase.get("input");
            String expected = testCase.get("expected");
            Map<String, Object> result = new HashMap<>();
            try {
                ProcessBuilder pb = new ProcessBuilder("python", "judge.py");
                Process process = pb.start();
                String json = String.format("{\"code\":%s,\"input\":%s}",
                    toJsonString(code), toJsonString(input));
                OutputStream os = process.getOutputStream();
                os.write(json.getBytes("UTF-8"));
                os.close();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();
                process.waitFor();
                result = parseJsonToMap(sb.toString());
                String actual = result.get("output") != null ? result.get("output").toString().trim() : "";
                boolean passed = actual.equals(expected != null ? expected.trim() : "");
                result.put("input", input);
                result.put("expected", expected);
                result.put("actual", actual);
                result.put("passed", passed);
            } catch (Exception e) {
                result.put("status", "error");
                result.put("stderr", e.getMessage());
                result.put("input", input);
                result.put("expected", expected);
                result.put("actual", "");
                result.put("passed", false);
            }
            results.add(result);
        }
        return results;
    }

    // 工具方法：字符串转JSON安全转义
    private String toJsonString(String s) {
        if (s == null) return "null";
        return '"' + s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + '"';
    }
    // 工具方法：简单JSON转Map（可用更强的库如Jackson）
    private Map<String, Object> parseJsonToMap(String json) {
        // 这里只做简单处理，建议生产用Jackson/Gson
        Map<String, Object> map = new HashMap<>();
        if (json == null || json.isEmpty()) return map;
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1);
            String[] pairs = json.split(",");
            for (String pair : pairs) {
                int idx = pair.indexOf(":");
                if (idx > 0) {
                    String key = pair.substring(0, idx).replaceAll("[\"{}]", "").trim();
                    String value = pair.substring(idx + 1).replaceAll("[\"{}]", "").trim();
                    map.put(key, value);
                }
            }
        }
        return map;
    }
} 