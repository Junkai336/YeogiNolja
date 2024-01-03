package com.example.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemberApplication.class, args);
	}

	@Bean(name = "uploadPath")
	public String uploadPath() {
//		return "D:/shop/item";
		return "/home/ec2-user/junkai/item";	// 임시경로
	}

}
