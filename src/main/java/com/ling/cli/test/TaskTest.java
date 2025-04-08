package com.ling.cli.test;

import java.util.TimerTask;

/**
 * @author ling
 * @description: TODO
 */
public class TaskTest extends TimerTask {
    @Override
    public void run() {
        System.out.println("定时任务执行了");
    }
}
