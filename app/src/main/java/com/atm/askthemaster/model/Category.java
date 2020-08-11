package com.atm.askthemaster.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Category {
    private String name;
    private List<String> childIds;
    private String parentId;
    private String cid;

    // Since user, asker, and master are using the same userId,
    // so masterIdList contains a list of userId
    private List<String> masterIdList;

    public Category() {
    }

    private Category(CategoryBuilder cb) {
        this.name = cb.name;
        this.childIds = cb.childIds;
        this.parentId = cb.parentId;
        this.cid = cb.cid;
        this.masterIdList = cb.masterIdList;
    }

    public String getName() {
        return name;
    }

    public List<String> getChildIds() {
        return childIds;
    }

    public String getParentId() {
        return parentId;
    }

    public String getCid() {
        return cid;
    }

    public List<String> getMasterIdList() {
        return masterIdList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(cid + " " + (parentId == null ? "null" : parentId) + " " + name + " " + (childIds == null ? "null" : childIds.toString()) + (masterIdList == null ? "null" : masterIdList.toString()));
        return sb.toString();
    }

    public static class CategoryBuilder {
        private String name;
        private List<String> childIds;
        private String parentId;
        private String cid;
        private List<String> masterIdList;

        public CategoryBuilder() {
        }

        public CategoryBuilder setName(String name) {
            this.name = name == null ? null : new String(name);
            return this;
        }

        public CategoryBuilder setChildIds(List<String> childIds) {
            this.childIds = childIds == null ? null : new ArrayList<>(childIds);
            return this;
        }

        public CategoryBuilder setParentId(String parentId) {
            this.parentId = parentId == null ? null : new String(parentId);
            return this;
        }

        public CategoryBuilder setCid(String cid) {
            this.cid = cid == null ? null : new String(cid);
            return this;
        }

        public CategoryBuilder setMasterIdList(List<String> masterIdList) {
            this.masterIdList = masterIdList == null ? null : new ArrayList<String>(masterIdList);
            return this;
        }

        public Category build() {
            return new Category(this);
        }

    }
}
