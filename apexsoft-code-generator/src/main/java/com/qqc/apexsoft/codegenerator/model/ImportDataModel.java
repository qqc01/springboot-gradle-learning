package com.qqc.apexsoft.codegenerator.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;
import java.util.Map;

/**
 * 导入数据模型
 */
public class ImportDataModel {
    private String name;
    private String desc;
    private ImportDataType type;
    private String procedureParam;
    private Map<Integer, String> row;
    private List<ImportDataModel> listData;
    private String listObjName;//首字母大写

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ImportDataType getType() {
        return type;
    }

    public void setType(ImportDataType type) {
        this.type = type;
    }

    public String getProcedureParam() {
        return procedureParam;
    }

    public void setProcedureParam(String procedureParam) {
        this.procedureParam = procedureParam;
    }

    public Map<Integer, String> getRow() {
        return row;
    }

    public void setRow(Map<Integer, String> row) {
        this.row = row;
    }

    public List<ImportDataModel> getListData() {
        return listData;
    }

    public void setListData(List<ImportDataModel> listData) {
        this.listData = listData;
    }

    public String getListObjName() {
        return listObjName;
    }

    public void setListObjName(String listObjName) {
        this.listObjName = listObjName;
    }

    @JSONField(serialize = false)
    public boolean isList() {
        return type.equals(ImportDataType.LIST);
    }

    @Override
    public String toString() {
        return "ImportDataModel{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", type=" + type +
                ", procedureParam='" + procedureParam + '\'' +
                ", row=" + row +
                ", listData=" + listData +
                ", listObjName='" + listObjName + '\'' +
                '}';
    }
}
