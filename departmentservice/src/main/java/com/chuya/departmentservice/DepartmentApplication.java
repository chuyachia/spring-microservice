package com.chuya.departmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {"com.chuya.departmentservice", "com.chuya.common"})
@EntityScan(basePackages = "com.chuya.common")
public class DepartmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(DepartmentApplication.class, args);
    }
}
