package com.ling.cli.handle;

/**
 * @author ling
 * @description: TODO
 */
public class Task1 extends LinkTaskConnector{

    @Override
    public boolean handle(TaskDataDto t) {
        System.out.printf("Task1 execute...  data:%s%n",t.getTask1Data());
        return super.handle(t);
    }
}
