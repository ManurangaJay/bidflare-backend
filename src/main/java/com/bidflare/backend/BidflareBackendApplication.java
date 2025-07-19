package com.bidflare.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
@SpringBootApplication
public class BidflareBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BidflareBackendApplication.class, args);
	}

}
