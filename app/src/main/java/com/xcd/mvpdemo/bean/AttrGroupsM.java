package com.xcd.mvpdemo.bean;

import java.util.List;

public class AttrGroupsM {
    private int id;
    private String name;
    private String origSpuName;
    private String description;
    private String mainImgUrl;
    private double maxPrice;
    private int categoryId;
    private String origCategoryName;
    private String origBrandName;
    private int brandId;
    private int seriesId;
    private String createTime;
    private String updateTime;
    private String height;
    private String width;
    private String length;
    private String weight;
    private List<ImagesDTO> images;
    private String metaTitle;
    private String metaDescription;
    private List<AttrGroupsDTO> attrGroups;//步骤父级
    private String productSchema;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigSpuName() {
        return origSpuName;
    }

    public void setOrigSpuName(String origSpuName) {
        this.origSpuName = origSpuName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainImgUrl() {
        return mainImgUrl;
    }

    public void setMainImgUrl(String mainImgUrl) {
        this.mainImgUrl = mainImgUrl;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getOrigCategoryName() {
        return origCategoryName;
    }

    public void setOrigCategoryName(String origCategoryName) {
        this.origCategoryName = origCategoryName;
    }

    public String getOrigBrandName() {
        return origBrandName;
    }

    public void setOrigBrandName(String origBrandName) {
        this.origBrandName = origBrandName;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(int seriesId) {
        this.seriesId = seriesId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public List<ImagesDTO> getImages() {
        return images;
    }

    public void setImages(List<ImagesDTO> images) {
        this.images = images;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public List<AttrGroupsDTO> getAttrGroups() {
        return attrGroups;
    }

    public void setAttrGroups(List<AttrGroupsDTO> attrGroups) {
        this.attrGroups = attrGroups;
    }

    public String getProductSchema() {
        return productSchema;
    }

    public void setProductSchema(String productSchema) {
        this.productSchema = productSchema;
    }

    public static class ImagesDTO {
        /**
         * id : 533
         * imgUrl : https://res.cloudinary.com/uc-technology-inc/image/upload/v1603420765/sjhs/service/product/PHONES/Apple_iPhone_12.jpg
         * defaultImg : 1
         */

        private int id;
        private String imgUrl;
        private String defaultImg;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getDefaultImg() {
            return defaultImg;
        }

        public void setDefaultImg(String defaultImg) {
            this.defaultImg = defaultImg;
        }
    }

    public static class AttrGroupsDTO {

        public AttrGroupsDTO(int attrGroupId, String attrGroupName, List<AttrsDTO> attrs, int attrId, String value, boolean isExpand) {
            this.attrGroupId = attrGroupId;
            this.attrGroupName = attrGroupName;
            this.attrs = attrs;
            this.attrId = attrId;
            this.value = value;
            this.isExpand = isExpand;
        }

        private int attrGroupId;//父级id
        private String attrGroupName;//父级名称
        private String description;
        private String icon;
        private String createTime;
        private String updateTime;
        private List<AttrsDTO> attrs;//子项列表
        private int attrId;//已经选择的子项id
        private String value;//已经选择的子项值
        private boolean isExpand;//是否已展开

        public int getAttrId() {
            return attrId;
        }

        public void setAttrId(int attrId) {
            this.attrId = attrId;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isExpand() {
            return isExpand;
        }

        public void setExpand(boolean expand) {
            isExpand = expand;
        }

        public int getAttrGroupId() {
            return attrGroupId;
        }

        public void setAttrGroupId(int attrGroupId) {
            this.attrGroupId = attrGroupId;
        }

        public String getAttrGroupName() {
            return attrGroupName;
        }

        public void setAttrGroupName(String attrGroupName) {
            this.attrGroupName = attrGroupName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public List<AttrsDTO> getAttrs() {
            return attrs;
        }

        public void setAttrs(List<AttrsDTO> attrs) {
            this.attrs = attrs;
        }

        public static class AttrsDTO {

            public AttrsDTO(int attrId, String icon, String value) {
                this.attrId = attrId;
                this.icon = icon;
                this.value = value;
            }

            private int attrId;//子项id
            private String attrName;
            private String icon;//子项图标
            private String value;//子项值
            private int attrType;
            private String desc;
            private int descPosition;
            private String createdAt;
            private String updatedAt;

            public int getAttrId() {
                return attrId;
            }

            public void setAttrId(int attrId) {
                this.attrId = attrId;
            }

            public String getAttrName() {
                return attrName;
            }

            public void setAttrName(String attrName) {
                this.attrName = attrName;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public int getAttrType() {
                return attrType;
            }

            public void setAttrType(int attrType) {
                this.attrType = attrType;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public int getDescPosition() {
                return descPosition;
            }

            public void setDescPosition(int descPosition) {
                this.descPosition = descPosition;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }
        }
    }
}
