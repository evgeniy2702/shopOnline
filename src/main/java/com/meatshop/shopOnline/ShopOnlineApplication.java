package com.meatshop.shopOnline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class ShopOnlineApplication  {

	public static void main(String[] args) {SpringApplication.run(ShopOnlineApplication.class, args);}
}
