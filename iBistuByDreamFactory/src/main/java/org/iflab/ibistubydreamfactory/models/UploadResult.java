package org.iflab.ibistubydreamfactory.models;

import java.util.List;

/**
 * 上传成功后返回的资源详情
 */
public class UploadResult extends BaseRecord {
    private List<UploadedResource> resource;

    public List<UploadedResource> getResource() {
        return resource;
    }

    public void setResource(List<UploadedResource> resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "UploadResult{" +
                "resource=" + resource +
                '}';
    }

    public static class UploadedResource {
        private String name;
        private String path;
        private String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "UploadedResource{" +
                    "name='" + name + '\'' +
                    ", path='" + path + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }
}
