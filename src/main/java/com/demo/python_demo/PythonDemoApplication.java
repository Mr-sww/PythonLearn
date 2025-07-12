package com.demo.python_demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.demo.python_demo.repository")
public class PythonDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(PythonDemoApplication.class, args);
    }
}