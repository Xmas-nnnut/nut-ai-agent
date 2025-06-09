package com.xqj.nutaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

class WebScrapingToolTest {

    @Test
    void testScrapeWebPage() {
        WebScrapingTool tool = new WebScrapingTool();
        String result = tool.scrapeWebPage("https://vcb-s.com/");
        System.out.println(result);
        assertNotNull(result);
    }
}
