package com.xqj.nutaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FileOperationToolTest {

    @Test
    public void readFile() {
        FileOperationTool tool = new FileOperationTool();
        String result = tool.readFile("test.txt");
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    public void writeFile() {
        FileOperationTool tool = new FileOperationTool();
        String result = tool.writeFile("test.txt", "测试test123");
        System.out.println(result);
        assertNotNull(result);
    }
}