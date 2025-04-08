package com.ling.cli.handle;

/**
 * @author ling
 * @description: TODO
 */
public class Task2 extends LinkTaskConnector{

    @Override
    public boolean handle(TaskDataDto t) {
        System.out.printf("Task2 execute...  data:%s%n",t.getTask2Data());
        return super.handle(t);
    }
}
