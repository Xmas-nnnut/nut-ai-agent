package com.xqj.nutaiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceDownloadToolTest {

    @Test
    void testDownloadResource() {
        ResourceDownloadTool tool = new ResourceDownloadTool();
        String url = "https://i0.hdslb.com/bfs/new_dyn/35d96aa29e8e06e0f7b9579ded48c59d512995925.jpg";
        String fileName = "test.jpg";
        String result = tool.downloadResource(url, fileName);
        assertNotNull(result);
    }
}
