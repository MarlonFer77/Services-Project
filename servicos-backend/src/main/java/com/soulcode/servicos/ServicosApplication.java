package com.soulcode.servicos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLOutput;

@SpringBootApplication
public class ServicosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicosApplication.class, args);

		System.out.println(new BCryptPasswordEncoder().encode("batata"));
	}

}
