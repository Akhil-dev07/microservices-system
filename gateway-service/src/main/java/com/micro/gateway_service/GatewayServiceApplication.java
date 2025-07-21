package com.micro.gateway_service;

import com.micro.gateway_service.filter.JwtAuthFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

	// ✅ Register JwtAuthFilter to protect specific routes by validating the token
	@Bean
	public FilterRegistrationBean<Filter> jwtFilter(JwtAuthFilter jwtAuthFilter) {
		FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();

		registrationBean.setFilter(jwtAuthFilter); // ✅ Add our custom filter

		// ✅ Apply the filter to all microservices' routes (except /api/auth/**)
		registrationBean.addUrlPatterns("/api/users/*", "/api/products/*", "/api/orders/*", "/api/payments/*");

		return registrationBean;
	}
}