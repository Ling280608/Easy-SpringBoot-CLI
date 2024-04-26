package com.ling.cli;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ling.cli.mapper")
public class EasySpringBootCliApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasySpringBootCliApplication.class, args);
    }

}
