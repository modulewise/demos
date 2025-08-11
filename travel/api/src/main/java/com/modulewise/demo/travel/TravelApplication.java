package com.modulewise.demo.travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories
public class TravelApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelApplication.class, args);
	}
}
