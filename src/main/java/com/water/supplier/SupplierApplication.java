package com.water.supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * springboot 项目启动类
 * @author wang-ql
 *
 */
@SpringBootApplication(scanBasePackages={"com.water"})
@ComponentScan("com.water")
public class SupplierApplication {
	public static void main(String[] args) {
		SpringApplication.run(SupplierApplication.class, args);
	}
}
