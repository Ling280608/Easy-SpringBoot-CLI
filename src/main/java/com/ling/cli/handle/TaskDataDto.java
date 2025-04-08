package com.ling.cli.handle;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ling
 * @description: 数据传递
 */
@Data
@Accessors(chain = true)
public class TaskDataDto {
    private String task1Data;
    private String task2Data;
    private String task3Data;
    private String commonData;
}
