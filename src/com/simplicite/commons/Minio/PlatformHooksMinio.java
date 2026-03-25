package com.simplicite.commons.Minio;

import java.io.File;
import java.nio.file.Files;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import org.json.JSONObject;

import com.simplicite.util.Grant;
import com.simplicite.util.AppLog;
import com.simplicite.util.tools.CloudStorageTool;

public class PlatformHooksMinio extends com.simplicite.util.engine.PlatformHooksInterface {
    private File getStorageFile(String path) {
        return path.startsWith("MinioTest") ? new File(path.replace("/", "~")) : null;
    }

    private CloudStorageTool getStorageTool() throws Exception {
        return new CloudStorageTool(Grant.getSystemAdmin().getJSONObjectParameter("MINIO_CONFIG"));
    }

    @Override
    public InputStream readDocument(String path) throws Exception {
        File f = getStorageFile(path);
        if (f == null)
            return super.readDocument(path);

        try (CloudStorageTool cst = getStorageTool()) {
            String n = f.getName();
            AppLog.info("[MINIO] reading " + n + " from S3", null);
            JSONObject sf = cst.get(n, true);
            return new ByteArrayInputStream((byte[])sf.get("content"));
        } catch (Exception e) {
            AppLog.error(null, e, null);
            return null;
        }
    }

    @Override
    public boolean writeDocument(String path, Object data) throws Exception {
        File f = getStorageFile(path);
        if (f == null)
            return super.writeDocument(path, data);

        try (CloudStorageTool cst = getStorageTool()) {
            String n = f.getName();
            AppLog.info("[MINIO] writing " + n + " to S3", null);
            cst.put(new JSONObject().put("name", n).put("mime", Files.probeContentType(f.toPath())).put("content", data));
            return true;
        } catch (Exception e) {
            AppLog.error(null, e, null);
            return false;
        }
    }

    @Override
    public boolean deleteDocument(String path) throws Exception {
        File f = getStorageFile(path);
        if (f == null)
            return super.deleteDocument(path);

        try (CloudStorageTool cst = getStorageTool()) {
            String n = f.getName();
            AppLog.info("[MINIO] deleting " + n + " from S3", null);
            cst.delete(n);
            return true;
        } catch (Exception e) {
            AppLog.error(null, e, null);
            return false;
        }
    }

}

