package com.ling.cli.handle;

import cn.hutool.core.util.ObjectUtil;

/**
 * @author ling
 * @description: TODO
 */
public class Task3 extends LinkTaskConnector{

    @Override
    public boolean handle(TaskDataDto t) {
        System.out.printf("Task3 execute...  data:%s%n",t.getTask3Data());
        return super.handle(t);
    }
}
