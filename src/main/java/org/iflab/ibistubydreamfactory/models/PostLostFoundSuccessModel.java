package org.iflab.ibistubydreamfactory.models;

import java.util.List;

/**
 * 发布新的lostfound成功后返回的信息
 */
public class PostLostFoundSuccessModel extends BaseRecord {
    private List<PostLostFoundResource> resource;

    public List<PostLostFoundResource> getResource() {
        return resource;
    }

    public void setResource(List<PostLostFoundResource> resource) {
        this.resource = resource;
    }

    public static class PostLostFoundResource {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
