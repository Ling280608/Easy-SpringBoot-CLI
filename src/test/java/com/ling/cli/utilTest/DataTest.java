package com.ling.cli.utilTest;

import com.ling.cli.utils.DataUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author ling
 * @description: TODO
 */
public class DataTest {

    @Test
    public void getData() {
        List<String> testData = new DataUtils().getTestData2();
        for (String s : testData) {
            System.out.println(s);
        }
    }
}
