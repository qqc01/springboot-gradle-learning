package com.qqc.apexsoft.codegenerator.constants;

public enum ItemTypeEnum {
    Controller_Append("Controller_Append"),
    Consumer_Append("Consumer_Append"),
    QueryModel("QueryModel"),
    Provider_Append_Page("Provider_Append_Page"),
    Provider_Append_Result("Provider_Append_Result"),
    Provider_Append_No_Result("Provider_Append_No_Result"),
    Dao_Append_Page_Or_Result("Dao_Append_Page_Or_Result"),
    Dao_Append_No_Result("Dao_Append_No_Result"),
    DaoImpl_Append_Page_Or_Result("DaoImpl_Append_Page_Or_Result"),
    DaoImpl_Append_No_Result("DaoImpl_Append_No_Result"),
    Mapper_Append_Page_Or_Result("Mapper_Append_Page_Or_Result"),
    Mapper_Append_No_Result("Mapper_Append_No_Result"),
    MapperXml_Append("MapperXml_Append"),
    Proto_Append("Proto_Append");
    private String itemName;
    private String itemDesc;

    ItemTypeEnum(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }
}
