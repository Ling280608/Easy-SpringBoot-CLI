package com.ling.cli;

import cn.hutool.core.date.DateUtil;
import com.ling.cli.test.TaskTest;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.Timer;

@SpringBootApplication
@MapperScan("com.ling.cli.mapper")
public class EasySpringBootCliApplication {

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TaskTest(), DateUtil.offsetMinute(new Date(),1));
        SpringApplication.run(EasySpringBootCliApplication.class, args);
    }

}
