package com.qqc.apexsoft.codegenerator.model;

public enum ImportDataType {
    /**
     * 普通字段
     */
    COMMON(1),

    /**
     * 集合
     */
    LIST(2);

    ImportDataType(int type) {
        this.type = type;
    }

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
