package com.ling.cli.handle;

/**
 * @author ling
 * @description: 任务执行器入口
 */
public class LinkTaskHandle {

    private final LinkTaskConnector firstTask;

    public LinkTaskHandle(){
        Task1 task1 = new Task1();
        Task2 task2 = new Task2();
        Task3 task3 = new Task3();
        task1.setNextTask(task2);
        task2.setNextTask(task3);
        this.firstTask = task1;
        System.out.println("任务链已构建完成");
    }

    public boolean executeTask(TaskDataDto taskDataDto){
        if (firstTask != null) return firstTask.handle(taskDataDto);
        else System.out.println("任务链未构建");
        return false;
    }
}
