package com.demo.python_demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@RestController
@RequestMapping("/api/judge")
public class CodeJudgeController {

    // 推荐用配置文件注入
    private static final String JUDGE0_URL = "https://judge0-ce.p.sulu.sh/submissions?base64_encoded=false&wait=true";
    private static final String JUDGE0_TOKEN = "sk_live_TL3e2S3vHYcrUtG5xPrUqTSIPzlsxf2l"; // Sulu Bearer Token

    @PostMapping("/run")
    public Map<String, Object> runCode(@RequestBody Map<String, Object> payload) {
        String code = (String) payload.get("code");
        String language = (String) payload.getOrDefault("language", "python");
        String input = (String) payload.getOrDefault("input", "");

        // 语言映射
        int languageId = getLanguageId(language);

        // 调用Judge0
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + JUDGE0_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("source_code", code);
        body.put("language_id", languageId);
        body.put("stdin", input);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(JUDGE0_URL, entity, Map.class);
            Map<String, Object> result = response.getBody();

            // 结构优化：只返回关键信息
            Map<String, Object> ret = new HashMap<>();
            ret.put("stdout", result.get("stdout"));
            ret.put("stderr", result.get("stderr"));
            ret.put("status", ((Map)result.get("status")).get("description"));
            ret.put("time", result.get("time"));
            ret.put("memory", result.get("memory"));
            return ret;
        } catch (Exception e) {
            Map<String, Object> err = new HashMap<>();
            err.put("status", "error");
            err.put("stderr", e.getMessage());
            return err;
        }
    }

    @PostMapping("/batch-judge")
    public List<Map<String, Object>> batchJudge(@RequestBody Map<String, Object> payload) {
        String code = (String) payload.get("code");
        String language = (String) payload.getOrDefault("language", "python");
        List<Map<String, String>> testCases = (List<Map<String, String>>) payload.get("testCases");

        int languageId = getLanguageId(language);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + JUDGE0_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<Map<String, Object>> results = new ArrayList<>();
        for (Map<String, String> testCase : testCases) {
            Map<String, Object> body = new HashMap<>();
            body.put("source_code", code);
            body.put("language_id", languageId);
            body.put("stdin", testCase.get("input"));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            try {
                ResponseEntity<Map> response = restTemplate.postForEntity(JUDGE0_URL, entity, Map.class);
                Map<String, Object> result = response.getBody();
                String actual = result.get("stdout") != null ? result.get("stdout").toString().trim() : "";
                String expected = testCase.get("expected") != null ? testCase.get("expected").trim() : "";
                boolean passed = actual.equals(expected);
                Map<String, Object> caseResult = new HashMap<>();
                caseResult.put("input", testCase.get("input"));
                caseResult.put("expected", expected);
                caseResult.put("actual", actual);
                caseResult.put("passed", passed);
                caseResult.put("stderr", result.get("stderr"));
                caseResult.put("status", ((Map)result.get("status")).get("description"));
                results.add(caseResult);
            } catch (Exception e) {
                Map<String, Object> err = new HashMap<>();
                err.put("input", testCase.get("input"));
                err.put("expected", testCase.get("expected"));
                err.put("actual", "");
                err.put("passed", false);
                err.put("error", e.getMessage());
                results.add(err);
            }
        }
        return results;
    }

    private int getLanguageId(String language) {
        switch (language.toLowerCase()) {
            case "python":
            case "python3":
                return 71;
            case "cpp":
            case "c++":
                return 54;
            case "java":
                return 62;
            // 可扩展更多语言
            default:
                return 71;
        }
    }
} 