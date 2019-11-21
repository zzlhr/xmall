package com.lhrsite.xshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.lhrsite.xshop.service",
        "com.lhrsite.xshop.mapper", "com.lhrsite.xshop.repository", "com.lhrsite.xshop.po",
        "com.lhrsite.xshop.core", "com.lhrsite.xshop.config",
        "com.lhrsite.xshop.webapi.controller"})
@EnableJpaRepositories("com.lhrsite.xshop.repository")
@EntityScan("com.lhrsite.xshop.po")
public class XshopWebApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(XshopWebApiApplication.class, args);
    }

}
