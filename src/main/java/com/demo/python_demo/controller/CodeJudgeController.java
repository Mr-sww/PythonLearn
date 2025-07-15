package com.demo.python_demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;
import java.io.*;

@RestController
@RequestMapping("/api/judge")
public class CodeJudgeController {

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