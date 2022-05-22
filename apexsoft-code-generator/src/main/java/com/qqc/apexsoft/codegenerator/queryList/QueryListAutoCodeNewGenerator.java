package com.qqc.apexsoft.codegenerator.queryList;

import com.qqc.apexsoft.codegenerator.common.AutoCodeGenerator;
import com.qqc.apexsoft.codegenerator.common.BasicAutoCodeGenerator;
import com.qqc.apexsoft.codegenerator.model.ImportDataModel;
import com.qqc.apexsoft.codegenerator.model.ImportDataType;
import com.qqc.apexsoft.codegenerator.utils.AutoCodeGeneratorException;
import com.qqc.apexsoft.codegenerator.utils.AutoCodeGeneratorHelper;
import com.qqc.apexsoft.codegenerator.utils.JSONUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 适用场景
 * 1、分页
 * 2、入参、出参包含list
 * 3、没有返回结果集
 * 4、新增内容和追加内容
 * 5、测试模块
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class QueryListAutoCodeNewGenerator extends BasicAutoCodeGenerator implements AutoCodeGenerator, InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(QueryListAutoCodeNewGenerator.class);
    private AutoCodeGeneratorHelper helper;

    static {
        setBasicClass(QueryListAutoCodeNewGenerator.class);
    }

    /**
     * 编写proto
     * <p>
     * 1、分页
     * 2、入参、出差包含list
     * 3、没有返回结果集
     * 4、新增/追加
     */
    public void writeProto() {
        // 1 创建文件
        if (getIsCreateFile()) {
            write0(getPath("proto"), getProtoNewString());
        }
        // 2 写入文件
        write2(getPath("proto"), getProtoAppendString(), getProtoRPCAppendString());
    }

    /**
     * 编写model
     * <p>
     * 1、分页
     * 2、入参包含list
     * 3、新增/追加
     */
    @Override
    public void writeModel() {
        mainName = "model";
        writeModel(getPath(mainName), inList);
    }

    /**
     * 编写controller
     * <p>
     * 1.Get/Post
     * 2.新增/追加
     */
    @Override
    public void writeController() {
        String appendControllerString = getAppendControllerString();
    }

    private String getAppendControllerString() {
        StringBuilder sb = new StringBuilder();
        if (isGet()) {
            sb.append("\t@RequestMapping(value = \"/v1/{0}\", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)\n");
        } else {
            sb.append("\t@ApiPost( \"/v1/{0}\")\n");
        }
        sb.append("\t@ApiOperation(value = \"{1}\")\n");
        sb.append("\tpublic {2}Rsp {0}(@ApiParam(value = \"入参JSON\") @RequestBody {2}Model model) {\n");
        sb.append("\t\treturn {3}.cusMaintainLocking(model);\n");
        sb.append("\t}\n");
        String controllerAppendString = replaceAll(sb, configuration.methodName, configuration.methodDesc, configuration.upperMethodName, getConsumerName());
        log.info("controllerAppendString:\n{}", controllerAppendString);
        return controllerAppendString;
    }

    @Override
    public void writeConsumer() {

    }

    @Override
    public void writeProvider() {

    }

    @Override
    public void writeDao() {

    }

    @Override
    public void writeDaoImpl() {

    }

    @Override
    public void writeMapper() {

    }

    @Override
    public void writeMapperXml() {

    }

    @Override
    public void writeClientTest() {

    }

    @Override
    public void writeServerTest() {

    }

    @Override
    public void writeTestData() {

    }

    private String getProtoBody(List<ImportDataModel> list) {
        return getProtoBody(list, whiteRegExp);
    }

    private String getProtoBody(List<ImportDataModel> list, String whiteRegExp) {
        StringBuilder result = new StringBuilder();
        String[] strings = new String[4];
        for (ImportDataModel importDataModel : list) {
            StringBuilder sb = new StringBuilder();
            String fieldName = importDataModel.getName();
            String fieldDesc = importDataModel.getDesc();
            String listObjName = importDataModel.getListObjName();
            if (importDataModel.getType().equals(ImportDataType.COMMON)) {
                if (!StringUtils.isBlank(whiteRegExp) && fieldName.matches(whiteRegExp)) {
                    continue;
                }
                strings[0] = fieldDesc;
                strings[1] = fieldName.equals("czr") ? "int32" : "string";
                strings[2] = fieldName;
                strings[3] = String.valueOf(fieldIndex++);
                // 操作人 int32 czr = 1;
                sb.append("\t// {0}\n").append("\t{1} {2} = {3};\n");
            }
            if (importDataModel.getType().equals(ImportDataType.LIST)) {
                strings[0] = "repeated";
                strings[1] = listObjName;
                strings[2] = fieldName;
                strings[3] = String.valueOf(fieldIndex++);
                // repeated CollaborgInfoNew2 list1 = 9;
                sb.append("\t{0} {1} {2} = {3};\n");
            }
            result.append(replaceAll(sb, strings));
        }
        return result.toString();
    }

    private String getProtoNewString() {
        StringBuilder sb = new StringBuilder();
        sb.append("syntax = \"proto3\";\n");
        sb.append("\n");
        sb.append("option java_multiple_files = true;\n");
        sb.append(replaceAll("package {0};\n", getPackageName("proto")));
        sb.append("\n");
        sb.append(replaceAll("service {0} {\n", getServiceName()));
        sb.append("}\n");
        log.info("构建protoNewString:\n{}", sb);
        return sb.toString();
    }

    public String getProtoRPCAppendString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t//{0}\n").append("\trpc {1} ({2}Req) returns ({2}Rsp) {};\n");
        String protoRPCAppendString = replaceAll(sb, configuration.methodDesc, configuration.methodName, configuration.upperMethodName);
        log.info("构建protoRPCAppendString:\n{}", protoRPCAppendString);
        return protoRPCAppendString;
    }

    public String getProtoAppendString() {
        StringBuilder sb = new StringBuilder();
        sb.append("/************************************** {0}开始 **************************************/\n");
        // 1 req
        sb.append("message {1}Req {\n");
        // 1.1 分页
        if (getPageEnabled()) {
            sb.append("\t//是否分页\n").append(replaceAll("\tint32 paging = {0};\n", fieldIndex++));
            sb.append("\t//页码\n").append(replaceAll("\tint32 current = {0};\n", fieldIndex++));
            sb.append("\t//页长\n").append(replaceAll("\tint32 pageSize = {0};\n", fieldIndex++));
            sb.append("\t//总记录数\n").append(replaceAll("\tint32 total = {0};\n", fieldIndex++));
            sb.append("\t//排序\n").append(replaceAll("\tstring sort = {0};\n", fieldIndex++));
        }
        // 1.2 默认必须有的入参
        sb.append("\t//操作人\n").append(replaceAll("\tint32 czr = {0};\n", fieldIndex++));
        sb.append(getProtoBody(inList));
        sb.append("}\n");
        // 1.3 空行
        sb.append("\n");

        // 2 rsp
        sb.append("message {1}Rsp {\n");
        sb.append(replaceAll("\tint32 code = {0}; //返回值\n", fieldIndex++));
        sb.append(replaceAll("\tstring note = {0}; //返回消息\n", fieldIndex++));
        // 2.1 分页
        if (getPageEnabled()) {
            sb.append(replaceAll("\tint32 total = {0}; //总记录数\n", fieldIndex++));
            sb.append(replaceAll("\trepeated {1}Record records = {0}; //返回结果集\n", fieldIndex++));
        }
        // 2.2 不分页返回结果集
        if (!getPageEnabled() && getIsReturnResult()) {
            // 2.3是否返回结果集
            sb.append(replaceAll("\trepeated {1}Record records = {0}; //返回结果集\n", fieldIndex++));
        }
        sb.append("}\n");
        // 2.4 空行
        sb.append("\n");

        // 3 rsp data
        sb.append("message {1}Record {\n");
        sb.append(getProtoBody(outList, "code|note"));
        sb.append("}\n");
        // 3.1 空行
        sb.append("\n");

        // 4 list
        for (ImportDataModel importDataModel : getAllList()) {
            String listObjName = importDataModel.getListObjName();
            sb.append("message " + listObjName + " {\n");
            sb.append(getProtoBody(importDataModel.getListData()));
            sb.append("}\n");
            // 4.1 空行
            sb.append("\n");
        }

        // 5 去除最后一个空行
        deleteEnd(sb);

        sb.append("/************************************** {0}结束 **************************************/\n");
        String protoAppendString = replaceAll(sb, configuration.methodName, configuration.upperMethodName);
        log.info("构建protoAppendString:\n{}", protoAppendString);
        return protoAppendString;
    }

    public void writeModel(String path, List<ImportDataModel> list) {
        if (CollectionUtils.isEmpty(list)) {
            throw new AutoCodeGeneratorException("list数据为空！！！");
        }
        write0(path, getModelNewString(list));
        List<ImportDataModel> childList = list.stream().filter(ImportDataModel::isList).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(childList)) {
            for (ImportDataModel importDataModel : childList) {
                setModelName(importDataModel.getListObjName());
                setModelDesc(importDataModel.getDesc());
                writeModel(getPath("model"), importDataModel.getListData());
            }
        }
    }

    private String getModelNewString(List<ImportDataModel> importDataModelList) {
        StringBuilder sb = new StringBuilder();
        sb.append("package {0};\n");
        sb.append("\n");
        sb.append("import com.apexsoft.crm.base.model.PagerModel;\n");
        sb.append("import io.swagger.annotations.ApiModel;\n");
        sb.append("import io.swagger.annotations.ApiModelProperty;\n");
        sb.append("\n");
        sb.append("import java.util.List;\n");
        sb.append("\n");
        sb.append("@ApiModel(description = \"{1}\")\n");
        // 分页控制
        if (getPageEnabled()) {
            sb.append("public class {2} extends PagerModel {\n");
        } else {
            sb.append("public class {2} {\n");
        }
        // 实例变量
        sb.append(getModelBody(importDataModelList));
        // getter and setter
        // 默认操作人
        sb.append("\n");
        sb.append(getModelGetterAndSetter(importDataModelList));
        // 去除最后一个空行
        deleteEnd(sb);
        sb.append("}\n");
        String newModelString = replaceAll(sb, getPackageName(mainName), getModelDesc(), getModelName());
        log.info("构建newModelString:\n{}", newModelString);
        return newModelString;
    }

    private String getModelBody(List<ImportDataModel> list) {
        return getModelBody(list, whiteRegExp);
    }

    private String getModelBody(List<ImportDataModel> list, String whiteRegExp) {
        StringBuilder sb = new StringBuilder();
        for (ImportDataModel importDataModel : list) {
            StringBuilder temp = new StringBuilder();
            String name = importDataModel.getName();
            String desc = importDataModel.getDesc();
            String listObjName = importDataModel.getListObjName();
            ImportDataType type = importDataModel.getType();
            if (name.matches(whiteRegExp)) {
                continue;
            }
            // list处理
            if (type.equals(ImportDataType.LIST)) {
                temp.append("\t/**\n").append("\t * {0}\n").append("\t */\n").append("\t@ApiModelProperty(notes = \"{0}\")\n").append("\tprivate List<{2}> {1};\n");
                temp.append("\n");
                sb.append(replaceAll(temp, desc, name, listObjName));
            } else {
                temp.append("\t/**\n").append("\t * {0}\n").append("\t */\n").append("\t@ApiModelProperty(notes = \"{0}\")\n").append("\tprivate String {1};\n");
                temp.append("\n");
                sb.append(replaceAll(temp, desc, name));
            }
        }
        return sb.toString();
    }

    private String getModelGetterAndSetter(List<ImportDataModel> list) {
        StringBuilder sb = new StringBuilder();
        for (ImportDataModel importDataModel : list) {
            StringBuilder temp = new StringBuilder();
            String name = importDataModel.getName();
            ImportDataType type = importDataModel.getType();
            String listObjName = importDataModel.getListObjName();
            if (name.matches(getWhiteRegExp())) {
                continue;
            }
            // list处理
            if (type.equals(ImportDataType.LIST)) {
                temp.append("\tpublic List<{2}> get{1}() {\n");
                temp.append("\t\treturn {0};\n");
                temp.append("\t}\n");
                temp.append("\n");
                temp.append("\tpublic void set{1}(List<{2}> {0}) {\n");
                temp.append("\t\tthis.{0} = {0};\n");
                temp.append("\t}\n");
                temp.append("\n");
                sb.append(replaceAll(temp, name, upper(name), listObjName));
            } else {
                temp.append("\tpublic String get{1}() {\n");
                temp.append("\t\treturn {0};\n");
                temp.append("\t}\n");
                temp.append("\n");
                temp.append("\tpublic void set{1}(String {0}) {\n");
                temp.append("\t\tthis.{0} = {0};\n");
                temp.append("\t}\n");
                temp.append("\n");
                sb.append(replaceAll(temp, name, upper(name)));
            }
        }
        return sb.toString();
    }

    public void config() {
        // 入参
        List<ImportDataModel> inList = getDataList(0);
        setInList(inList);
        log.info("setInList:{}", JSONUtil.getJSONStandardString(inList));
        // 出参
        List<ImportDataModel> outList = getDataList(1);
        setOutList(outList);
        log.info("setOutList:{}", JSONUtil.getJSONStandardString(outList));
    }

    @Override
    protected boolean isList(Object obj) {
        return String.valueOf(obj).matches(".*(l|L)ist.*");
    }

    /**
     * 是否Get请求，默认false
     */
    private boolean isGet = false;

    public boolean isGet() {
        return isGet;
    }

    public void setGet(boolean get) {
        isGet = get;
    }

    @Override
    public void afterPropertiesSet() {
        fileCheck();
    }
}
