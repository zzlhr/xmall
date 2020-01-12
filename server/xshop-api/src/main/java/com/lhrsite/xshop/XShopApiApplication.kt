package com.lhrsite.xshop

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.io.File

@SpringBootApplication(scanBasePackages = ["com.lhrsite.xshop.service", "com.lhrsite.xshop.mapper",
    "com.lhrsite.xshop.repository", "com.lhrsite.xshop.po", "com.lhrsite.xshop.core",
    "com.lhrsite.xshop.config", "com.lhrsite.xshop.controller"])
@EnableJpaRepositories("com.lhrsite.xshop.repository")
@EntityScan("com.lhrsite.xshop.po")
class XShopApiApplication


fun main(args: Array<String>) {
    runApplication<XShopApiApplication>(*args)
}


