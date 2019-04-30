package com.lhrsite.xshop.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.lhrsite.xshop.service",
        "com.lhrsite.xshop.mapper", "com.lhrsite.xshop.repository", "com.lhrsite.xshop.po",
        "com.lhrsite.xshop.core","com.lhrsite.xshop.api.controller"})
@EnableJpaRepositories("com.lhrsite.xshop.repository")
@MapperScan("com.lhrsite.xshop.mapper")
@EntityScan("com.lhrsite.xshop.po")
public class XshopAppApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(XshopAppApiApplication.class, args);
    }

}
