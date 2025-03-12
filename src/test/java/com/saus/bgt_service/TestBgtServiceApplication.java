package com.saus.bgt_service;

import org.springframework.boot.SpringApplication;

public class TestBgtServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(BgtServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
