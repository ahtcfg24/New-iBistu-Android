package org.iflab.ibistubydreamfactory.models;

import java.util.List;

/**
 * 上传文件请求体
 */
public class UploadFileRequestBody extends BaseRecord {
    private List<UploadResource> resource;

    public List<UploadResource> getResource() {
        return resource;
    }

    public void setResource(List<UploadResource> resource) {
        this.resource = resource;
    }

    public static class UploadResource {
        private String name;
        private String type;
        private boolean is_base64;
        private String content;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isIs_base64() {
            return is_base64;
        }

        public void setIs_base64(boolean is_base64) {
            this.is_base64 = is_base64;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
