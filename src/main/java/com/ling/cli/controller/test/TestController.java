package com.ling.cli.controller.test;

import com.ling.cli.annotation.DistributedLock;
import com.ling.cli.models.exception.WarningException;
import com.ling.cli.models.global.SysResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ling
 * @description: TODO
 */
@Slf4j
@RestController
@RequestMapping("test")
public class TestController {

    public static int i = 0;

    @GetMapping("hello")
    @DistributedLock(prefix = "test:",key = "add",waitTime = 10,leaseTime = 50)
    public SysResult hello(@RequestParam String name) throws InterruptedException {
        int j = i;
        Thread.sleep(20);
        i = j + 1;
        System.out.println(i);
        return SysResult.success("hello " + name);
    }
}
