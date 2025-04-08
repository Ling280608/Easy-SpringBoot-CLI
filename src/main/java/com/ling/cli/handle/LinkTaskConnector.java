package com.ling.cli.handle;

import lombok.Data;

/**
 * @author ling
 * @description: TODO
 */
@Data
public abstract class LinkTaskConnector {

    private LinkTaskConnector nextTask;

    public boolean handle(TaskDataDto t) {
        if (nextTask == null) return true;

        return nextTask.handle(t);
    }
}
