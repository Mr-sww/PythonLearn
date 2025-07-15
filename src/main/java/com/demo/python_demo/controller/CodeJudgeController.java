package com.demo.python_demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/judge")
public class CodeJudgeController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/run")
    public Map<String, Object> runCode(@RequestBody Map<String, Object> payload) {
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
            // 获取 judge.py 绝对路径
            String judgePath = System.getProperty("user.dir") + File.separator + "judge.py";
            ProcessBuilder pb = new ProcessBuilder("python", judgePath);
            Process process = pb.start();
            // 传递参数
            String json = String.format("{\"code\":%s,\"input\":%s}",
                toJsonString(code), toJsonString(input));
            System.out.println("发送给 judge.py 的 JSON: " + json); // 新增日志
            OutputStream os = process.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();
            System.out.println("已写入 judge.py"); // 新增日志
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            reader.close();
            process.waitFor();
            System.out.println("judge.py输出: " + sb.toString()); // 日志输出
            // 新增：打印 judge.py 的错误输出
            BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
            StringBuilder errSb = new StringBuilder();
            String errLine;
            while ((errLine = errReader.readLine()) != null) errSb.append(errLine).append("\n");
            errReader.close();
            System.out.println("judge.py 错误输出: " + errSb.toString());
            ret = parseJsonToMap(sb.toString());
            // 如果 ret 为空或无 output/stderr 字段，返回错误提示
            if (ret == null || ret.isEmpty() || (ret.get("output") == null && ret.get("stderr") == null)) {
                ret = new HashMap<>();
                ret.put("stderr", "判题脚本无输出或输出格式错误");
                ret.put("status", "error");
            }
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常堆栈
            ret.put("status", "error");
            ret.put("stderr", e.getMessage());
        }
        return ret;
    }

    @PostMapping("/batch-judge")
    public List<Map<String, Object>> batchJudge(@RequestBody Map<String, Object> payload) {
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
    // 工具方法：用 Jackson 解析 JSON 字符串为 Map
    private Map<String, Object> parseJsonToMap(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            Map<String, Object> err = new HashMap<>();
            err.put("stderr", "JSON解析失败: " + e.getMessage());
            return err;
        }
    }
} 