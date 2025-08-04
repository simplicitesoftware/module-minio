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
        AppLog.info("[MINIO] path = " + path, null);
        return path.startsWith("MinioTest") ? new File(path.replace("/", "~")) : null;
    }

    private CloudStorageTool getStorageTool() throws Exception {
        return new CloudStorageTool(Grant.getSystemAdmin().getJSONObjectParameter("MINIO_CONFIG"));
    }

    @Override
    public InputStream readDocument(String path) throws Exception {
        File f = getStorageFile(path);
        if (f == null)
            return null;

        try (CloudStorageTool cst = getStorageTool()) {
            JSONObject sf = cst.get(f.getName(), true);
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
            return false;

        try (CloudStorageTool cst = getStorageTool()) {
            cst.put(new JSONObject().put("name", f.getName()).put("mime", Files.probeContentType(f.toPath())).put("content", data));
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
            return false;

        try (CloudStorageTool cst = getStorageTool()) {
            cst.delete(f.getName());
            return true;
        } catch (Exception e) {
            AppLog.error(null, e, null);
            return false;
        }
    }

}
