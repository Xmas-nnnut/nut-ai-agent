package com.xqj.nutaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PDFGenerationToolTest {

    @Test
    public void testGeneratePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "test.pdf";
        String content = "那我问你 114514 aaa";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }

    @Test
    public void testGeneratePDFFromMarkdown() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "test-markdown.pdf";
        String markdownContent = """
                # 这是一个标题
                
                这是一个段落，包含**粗体文本**和*斜体文本*。
                
                ## 二级标题
                
                这里是一个列表：
                - 第一项
                - 第二项
                - 第三项
                
                这里是一个编号列表：
                1. 第一步
                2. 第二步
                3. 第三步
                
                这里是一段代码：`System.out.println("Hello World");`
                
                ### 表格示例
                
                | 姓名 | 年龄 | 城市 |
                |------|------|------|
                | 张三 | 25   | 北京 |
                | 李四 | 30   | 上海 |
                | 王五 | 28   | 广州 |
                
                > 这是一个引用块
                > 可以包含多行内容
                
                ### 链接示例
                
                [访问百度](https://www.baidu.com)
                """;
        
        String result = tool.generatePDF(fileName, markdownContent);
        assertNotNull(result);
        assertTrue(result.contains("PDF generated successfully from Markdown"));
    }
}
