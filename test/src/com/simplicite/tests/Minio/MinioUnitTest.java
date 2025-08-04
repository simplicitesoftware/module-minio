package com.simplicite.tests.Minio;

import org.json.JSONObject;

import java.util.Date;

import com.simplicite.util.AppLog;
import com.simplicite.util.Grant;
import com.simplicite.util.tools.HTTPTool;
import com.simplicite.util.tools.CloudStorageTool;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests MinioUnitTest
 */
public class MinioUnitTest {
    private static final String NAME = "test.txt";
    private static final String MIME = HTTPTool.MIME_TYPE_TXT;
    private static final String CONTENT = "Hello world (" + new Date() + ")";
    
    @Test
    public void test() {
        Grant sys = Grant.getSystemAdmin();

        String config =  sys.getParameter("MINIO_CONFIG");
        AppLog.debug("MINIO config: " + config, null);

        try (CloudStorageTool cst = new CloudStorageTool(sys, new JSONObject(config))) {
            JSONObject file = new JSONObject()
                .put("name", NAME)
                .put("mime", MIME)
                .put("content", CONTENT);

            String res = cst.put(file);
            AppLog.debug("MINIO put result " + res, sys);
            
            file = cst.get(NAME, true);
            AppLog.debug("MINIO get result " + file.toString(2), sys);
            String content = new String((byte[])file.get("content"));
            AppLog.info("MINIO file content " + content, sys);
            assertEquals(CONTENT, content);

            cst.delete(NAME);
        } catch (Exception e) {
            AppLog.error(null, e, sys);
            fail(e.getMessage());
        }
    }
}
