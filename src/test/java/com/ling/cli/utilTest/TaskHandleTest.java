package com.ling.cli.utilTest;

import com.ling.cli.handle.LinkTaskHandle;
import com.ling.cli.handle.TaskDataDto;
import org.junit.jupiter.api.Test;

/**
 * @author ling
 * @description: TODO
 */
public class TaskHandleTest {

    @Test
    public void test() {
        TaskDataDto taskDataDto = new TaskDataDto()
                .setTask1Data("hello task1")
                .setTask2Data("hello task2")
                .setTask3Data("hello task3");
        LinkTaskHandle linkTaskHandle = new LinkTaskHandle();
        Boolean isSuccess = linkTaskHandle.executeTask(taskDataDto);
        System.out.println("isSuccess = " + isSuccess);
    }
}
