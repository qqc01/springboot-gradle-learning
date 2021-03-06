package com.qqc.apexsoft.codegenerator.queryList;

import com.qqc.apexsoft.codegenerator.common.AutoCodeGenerator;
import com.qqc.apexsoft.codegenerator.common.BasicAutoCodeGenerator;
import com.qqc.apexsoft.codegenerator.model.ImportDataModel;
import com.qqc.apexsoft.codegenerator.model.ImportDataType;
import com.qqc.apexsoft.codegenerator.utils.AutoCodeGeneratorException;
import com.qqc.apexsoft.codegenerator.utils.AutoCodeGeneratorHelper;
import com.qqc.apexsoft.codegenerator.utils.JSONUtil;
import javafx.scene.input.DataFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
        if (isCreateFile()) {
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
        mainName = "controller";
        // 1 创建文件
        if (isCreateFile()) {
            write0(getPath(mainName), getControllerNewString());
        }
        // 2 写入文件
        write0(getPath(mainName), getControllerAppendString());
    }

    /**
     * 编写consumer
     * <p>
     * 1.新增/追加
     */
    @Override
    public void writeConsumer() {
        mainName = "consumer";
        // 1 创建文件
        if (isCreateFile()) {
            write0(getPath(mainName), getConsumerNewString());
        }
        // 2 写入文件
        write0(getPath(mainName), getConsumerAppendString());
    }

    /**
     * 编写provider
     * <p>
     * 1、分页
     * 2、入参、出差包含list（建议定制开发，不支持）
     * 3、没有返回结果集
     * 4、新增/追加
     */
    @Override
    public void writeProvider() {
        mainName = "provider";
        // 1 创建文件
        if (isCreateFile()) {
            write0(getPath(mainName), getProviderNewString());
        }
        // 2 写入文件
        write0(getPath(mainName), getProviderAppendString());
    }

    /**
     * 编写dao
     * <p>
     * 1、没有返回结果集
     * 2、新增/追加
     */
    @Override
    public void writeDao() {
        mainName = "dao";
        // 1 创建文件
        if (isCreateFile()) {
            write0(getPath(mainName), getDaoNewString());
        }
        // 2 写入文件
        write0(getPath(mainName), getDaoAppendString());
    }

    /**
     * 编写daoImpl
     * <p>
     * 1、没有返回结果集
     * 2、新增/追加
     */
    @Override
    public void writeDaoImpl() {
        mainName = "daoImpl";
        // 1 创建文件
        if (isCreateFile()) {
            write0(getPath(mainName), getDaoImplNewString());
        }
        // 2 写入文件
        write0(getPath(mainName), getDaoImplAppendString());
    }

    /**
     * 编写mapper
     * <p>
     * 1、没有返回结果集
     * 2、新增/追加
     */
    @Override
    public void writeMapper() {
        mainName = "mapper";
        // 1 创建文件
        if (isCreateFile()) {
            write0(getPath(mainName), getMapperNewString());
        }
        // 2 写入文件
        write0(getPath(mainName), getMapperAppendString());
    }

    /**
     * 编写mapperXml
     * <p>
     * 1、分页
     * 2、没有返回结果集
     * 3、新增/追加
     */
    @Override
    public void writeMapperXml() {
        mainName = "mapperXml";
        // 1 创建文件
        if (isCreateFile()) {
            write0(getPath(mainName), getMapperXmlNewString());
        }
        // 2 写入文件
        write0(getPath(mainName), getMapperXmlAppendString());
    }

    @Override
    public void writeClientTest() {
        mainName = "clientTest";
        // 1 创建文件
        if (isCreateFile()) {
            write0(getPath(mainName), getClientTestNewString());
        }
        // 2 写入文件
        write0(getPath(mainName), getClientTestAppendString());
    }

    @Override
    public void writeServerTest() {
        mainName = "serverTest";
        // 1 创建文件
        if (isCreateFile()) {
            write0(getPath(mainName), getServerTestNewString());
        }
        // 2 写入文件
        write0(getPath(mainName), getServerTestAppendString());
    }

    @Override
    public void writeTestData() {
        mainName = "testData";
        write0(getPath(mainName), getTestDataNewString());
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

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format(new Date());
    }

    private String getCopyRightString(int replaceCount) {
        StringBuilder sb = new StringBuilder();
        sb.append("/**\n");
        sb.append(" * @author qqc\n");
        sb.append(replaceFormat(" * @create {}\n", replaceCount));
        sb.append(" * @description\n");
        sb.append(" */\n");
        return sb.toString();
    }

    @Override
    public void afterPropertiesSet() {
        fileCheck();
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

    public String getProtoAppendString() {
        StringBuilder sb = new StringBuilder();
        sb.append("/************************************** {0}开始 **************************************/\n");
        // 1 req
        sb.append("message {1}Req {\n");
        // 1.1 分页
        if (pageEnabled()) {
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
        // 2.1 分页和结果集返回
        if (pageEnabled()) {
            sb.append(replaceAll("\tint32 total = {0}; //总记录数\n", fieldIndex++));
        }
        if (pageEnabled() || isReturnResult()) {
            sb.append(replaceAll("\trepeated {1}Record records = {0}; //返回结果集\n", fieldIndex++));
            sb.append("}\n");
            sb.append("\n");
            // 3 rsp data
            sb.append("message {1}Record {\n");
            sb.append(getProtoBody(outList, "code|note"));
            sb.append("}\n");
            // 3.1 空行
            sb.append("\n");
        } else {
            sb.append("}\n");
            // 2.4 空行
            sb.append("\n");
        }

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

    public String getProtoRPCAppendString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t//{0}\n").append("\trpc {1} ({2}Req) returns ({2}Rsp) {};\n");
        String protoRPCAppendString = replaceAll(sb, configuration.methodDesc, configuration.methodName, configuration.upperMethodName);
        log.info("构建protoRPCAppendString:\n{}", protoRPCAppendString);
        return protoRPCAppendString;
    }

    private String getProtoBody(List<ImportDataModel> list) {
        return getProtoBody(list, configuration.getWhiteRegExp());
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
        if (pageEnabled()) {
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
        return getModelBody(list, configuration.getWhiteRegExp());
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
            if (name.matches(configuration.getWhiteRegExp())) {
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

    private String getControllerNewString() {
        int replaceCount = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(replaceFormat("package {};\n", replaceCount++));
        sb.append("\n");
        sb.append("import com.apexsoft.crm.ams.annotation.ApiPost;\n");
        sb.append(replaceFormat("import {}.*;\n", replaceCount++));
        sb.append(replaceFormat("import {}.{}Consumer;\n", replaceCount++, replaceCount++));
        sb.append("import io.swagger.annotations.Api;\n");
        sb.append("import io.swagger.annotations.ApiOperation;\n");
        sb.append("import io.swagger.annotations.ApiParam;\n");
        sb.append(replaceFormat("import {}.*;\n", replaceCount++));
        sb.append("import org.slf4j.Logger;\n");
        sb.append("import org.slf4j.LoggerFactory;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.web.bind.annotation.PostMapping;\n");
        sb.append("import org.springframework.web.bind.annotation.RequestBody;\n");
        sb.append("import org.springframework.web.bind.annotation.RequestMapping;\n");
        sb.append("import org.springframework.web.bind.annotation.RestController;\n");
        sb.append("\n");
        sb.append("/**\n");
        sb.append(" * @author qqc\n");
        sb.append(replaceFormat(" * @create {}\n", replaceCount++));
        sb.append(" * @description\n");
        sb.append(" */\n");
        sb.append("@RestController\n");
        sb.append(replaceFormat("@RequestMapping(value = \"/{}\")\n", replaceCount++));
        sb.append(replaceFormat("@Api(tags = \"{}\")\n", replaceCount++));
        sb.append(replaceFormat("public class {}Controller {\n", 3));
        sb.append(replaceFormat("\tprivate static final Logger log = LoggerFactory.getLogger({}Controller.class);\n", 3));
        sb.append("\n");
        sb.append("\t@Autowired\n");
        sb.append(replaceFormat("\tprivate {}Consumer {}Consumer;\n", 3, 6));
        // 补充第一个接口时的空行
        if (configuration.getInterfaceIndex() == 1) {
            sb.append("\n");
        }
        sb.append("}\n");
        String controllerNewString = replaceAll(sb,
                getPackageName(mainName),
                getPackageName("model"),
                getPackageName("consumer"),
                configuration.upperFunctionName,
                getPackageName("proto"),
                getTime(),
                configuration.functionName,
                configuration.functionDesc);
        log.info("controllerNewString:\n{}", controllerNewString);
        return controllerNewString;
    }

    private String getControllerAppendString() {
        int replaceCount = 0;
        StringBuilder sb = new StringBuilder();
        if (isGet()) {
            sb.append(replaceFormat("\t@RequestMapping(value = \"/v1/{}\", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)\n", replaceCount++));
        } else {
            sb.append(replaceFormat("\t@ApiPost( \"/v1/{}\")\n", replaceCount++));
        }
        sb.append(replaceFormat("\t@ApiOperation(value = \"{}\")\n", replaceCount++));
        sb.append(replaceFormat("\tpublic {}Rsp {}(@ApiParam(value = \"入参JSON\") @RequestBody {}Model model) {\n", replaceCount++, 0, 2));
        sb.append(replaceFormat("\t\treturn {}Consumer.{}(model);\n", replaceCount++, 0));
        sb.append("\t}\n");
        String controllerAppendString = replaceAll(sb,
                configuration.methodName,
                configuration.methodDesc,
                configuration.upperMethodName,
                configuration.functionName);
        log.info("controllerAppendString:\n{}", controllerAppendString);
        return controllerAppendString;
    }

    private String getConsumerNewString() {
        int replaceCount = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(replaceFormat("package {}.service;\n", replaceCount++));
        sb.append("\n");
        sb.append("import com.apex.ams.annotation.AmsBlockingStub;\n");
        sb.append("import com.apexsoft.crm.auth.util.UserHelper;\n");
        sb.append(replaceFormat("import {}.model.*;\n", 0));
        sb.append("import com.apexsoft.utils.ParamUtils;\n");
        sb.append("import com.apexsoft.utils.ProtoBufUtil;\n");
        sb.append(replaceFormat("import {}.*;\n", replaceCount++));
        sb.append("import org.apache.commons.lang3.StringUtils;\n");
        sb.append("import org.slf4j.Logger;\n");
        sb.append("import org.slf4j.LoggerFactory;\n");
        sb.append("\n");
        sb.append("import java.util.List;\n");
        sb.append("\n");
        sb.append("/**\n");
        sb.append(" * @author qqc\n");
        sb.append(replaceFormat(" * @create {}\n", replaceCount++));
        sb.append(" * @description\n");
        sb.append(" */\n");
        sb.append("@Service\n");
        sb.append(replaceFormat("public class {}Consumer {\n", replaceCount++));
        sb.append(replaceFormat("\tprivate static final Logger log = LoggerFactory.getLogger({}Consumer.class);\n", 3));
        sb.append("\n");
        sb.append("\t@AmsBlockingStub\n");
        sb.append(replaceFormat("\tprivate {}ServiceGrpc.{}ServiceBlockingStub {}ServiceBlockingStub;\n", 3, 3, replaceCount++));
        // 补充第一个接口时的空行
        if (configuration.getInterfaceIndex() == 1) {
            sb.append("\n");
        }
        sb.append("}\n");
        String consumerNewString = replaceAll(sb,
                configuration.defaultPackageNamePrefix,
                getPackageName("proto"),
                getTime(),
                configuration.upperFunctionName,
                configuration.functionName);
        log.info("consumerNewString:\n{}", consumerNewString);
        return consumerNewString;
    }

    private String getConsumerAppendString() {
        int replaceCount = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(replaceFormat("\tpublic {}Rsp {}({}Model model) {\n", replaceCount++, replaceCount++, 0));
        sb.append(replaceFormat("\t\t{}Req.Builder request = {}Req.newBuilder();\n", 0, 0));
        sb.append("\t\tProtoBufUtil.transform(model, request);\n");
        sb.append("\t\trequest.setCzr(UserHelper.getId());\n");
        sb.append(replaceFormat("\t\treturn {}ServiceBlockingStub.{}(request.build());\n", replaceCount++, replaceCount++));
        sb.append("\t}\n");
        String consumerAppendString = replaceAll(sb,
                configuration.upperMethodName,
                configuration.methodName,
                configuration.functionName,
                configuration.methodName);
        log.info("consumerAppendString:\n{}", consumerAppendString);
        return consumerAppendString;
    }

    private String getProviderNewString() {
        int replaceCount = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(replaceFormat("package {}.provider;\n", replaceCount++));
        sb.append("\n");
        sb.append("import com.apex.ams.server.AmsService;\n");
        sb.append(replaceFormat("import {}.dao.{}Dao;\n", 0, replaceCount++));
        sb.append("import com.apexsoft.utils.ParamUtils;\n");
        sb.append("import com.apexsoft.utils.ProtoBufUtil;\n");
        sb.append("import io.grpc.Status;\n");
        sb.append("import io.grpc.StatusException;\n");
        sb.append("import io.grpc.stub.StreamObserver;\n");
        sb.append(replaceFormat("import {}.*;\n", replaceCount++));
        sb.append("import org.apache.commons.collections.CollectionUtils;\n");
        sb.append("import org.apache.commons.lang3.StringUtils;\n");
        sb.append("import org.slf4j.Logger;\n");
        sb.append("import org.slf4j.LoggerFactory;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("\n");
        sb.append("import java.util.List;\n");
        sb.append("\n");
        sb.append(getCopyRightString(replaceCount++));
        sb.append("@AmsService\n");
        sb.append(replaceFormat("public class {}Provider extends {}ServiceGrpc.{}ServiceImplBase{\n", 1, 1, 1));
        sb.append(replaceFormat("\tprivate static final Logger log = LoggerFactory.getLogger({}Provider.class);\n", 1));
        sb.append("\t@Autowired\n");
        sb.append(replaceFormat("\tprivate {}Dao {}Dao;\n", 1, replaceCount++));
        // 补充第一个接口时的空行
        if (configuration.getInterfaceIndex() == 1) {
            sb.append("\n");
        }
        sb.append("}\n");
        String providerNewString = replaceAll(sb,
                configuration.defaultPackageNamePrefix,
                configuration.upperFunctionName,
                getPackageName("proto"),
                getTime(),
                configuration.functionName);
        log.info("providerNewString:\n{}", providerNewString);
        return providerNewString;
    }

    private String getProviderAppendString() {
        int replaceCount = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("\t@Override\n");
        sb.append(replaceFormat("\tpublic void {}({}Req request, StreamObserver<{}Rsp> responseObserver) {\n", replaceCount++, replaceCount++, 1));
        sb.append(replaceFormat("\t\t{}Rsp.Builder rsp = {}Rsp.newBuilder();\n", 1, 1));
        sb.append("\t\tMap<String, Object> ins = new HashMap<>();\n");
        // 分页
        if (pageEnabled()) {
            sb.append("\t\tins.put(\"I_PAGING\", request.getPaging());\n");
            sb.append("\t\tins.put(\"I_PAGENO\", request.getCurrent());\n");
            sb.append("\t\tins.put(\"I_PAGELENGTH\", request.getPageSize());\n");
            sb.append("\t\tins.put(\"I_TOTALROWS\", request.getTotal());\n");
            sb.append("\t\tins.put(\"I_SORT\", request.getSort());\n");
        }
        // 默认必传的 crz
        sb.append("\t\tins.put(\"I_CZR\", request.getCzr());\n");
        sb.append(getProviderBody(inList));
        // 是否返回结果集
        if (pageEnabled() || isReturnResult()) {
            sb.append(replaceFormat("\t\tList<{}Record.Builder> resultList = {}Dao.{}(ins);\n", 1, 2, 0));
            // 是否分页字段
            if (pageEnabled()) {
                sb.append("\t\tMap<String, Object> map = ProtoBufUtil.transformMap(ins);\n");
            } else {
                sb.append("\t\tMap<String, Object> map = ProtoBufUtil.transformMap2(ins);\n");
            }
            sb.append("\t\tProtoBufUtil.transform(map, rsp);\n");
            // 遍历添加结果集
            sb.append("\t\tresultList.forEach(builder -> {\n");
            sb.append("\t\t\tif (builder != null) {\n");
            sb.append("\t\t\t\trsp.addRecords(builder);\n");
            sb.append("\t\t\t}\n");
            sb.append("\t\t});\n");
        } else {
            sb.append(replaceFormat("\t\t{}Dao.{}(ins);\n", 2, 0));
            sb.append("\t\tMap<String, Object> map = ProtoBufUtil.transformMap2(ins);\n");
            sb.append("\t\tProtoBufUtil.transform(map, rsp);\n");
        }
        sb.append("\t\tresponseObserver.onNext(rsp.build());\n");
        sb.append("\t\tresponseObserver.onCompleted();\n");
        sb.append("\t}\n");
        String providerAppendString = replaceAll(sb,
                configuration.methodName,
                configuration.upperMethodName,
                configuration.functionName);
        log.info("providerAppendString:\n{}", providerAppendString);
        return providerAppendString;
    }

    private String getProviderBody(List<ImportDataModel> importDataModelList) {
        StringBuilder sb = new StringBuilder();
        for (ImportDataModel importDataModel : importDataModelList) {
            if (importDataModel.isList() || (StringUtils.isNotBlank(configuration.getWhiteRegExp())
                    && importDataModel.getName().matches(configuration.getWhiteRegExp()))) {
                continue;
            }
            sb.append(replaceAll("\t\tins.put(\"{0}\", request.get{1}());\n", importDataModel.getProcedureParam(), upper(importDataModel.getName())));
        }
        return sb.toString();
    }

    private String getDaoNewString() {
        int replaceCount = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(replaceFormat("package {}.dao;\n", replaceCount++));
        sb.append(replaceFormat("import {}.*;\n", replaceCount++));
        sb.append("\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.Map;\n");
        sb.append("\n");
        sb.append(getCopyRightString(replaceCount++));
        sb.append(replaceFormat("public interface {}Dao {\n", replaceCount++));
        sb.append("}\n");
        String daoNewString = replaceAll(sb,
                configuration.defaultPackageNamePrefix,
                getPackageName("proto"),
                getTime(),
                configuration.upperFunctionName);
        log.info("daoNewString:\n{}", daoNewString);
        return daoNewString;
    }

    private String getDaoAppendString() {
        int replaceCount = 0;
        StringBuilder sb = new StringBuilder();
        String daoAppendString = null;
        if (pageEnabled() || isReturnResult()) {
            sb.append(replaceFormat("\tList<{}Record.Builder> {}(Map<String, Object> ins);\n", replaceCount++, replaceCount++));
            daoAppendString = replaceAll(sb, configuration.upperMethodName, configuration.methodName);
        } else {
            sb.append(replaceFormat("\tvoid {}(Map<String, Object> ins);\n", replaceCount++));
            daoAppendString = replaceAll(sb, configuration.methodName);
        }
        log.info("daoAppendString:\n{}", daoAppendString);
        return daoAppendString;
    }

    private String getDaoImplNewString() {
        int replaceCount = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(replaceFormat("package {}.dao.impl;\n", replaceCount++));
        sb.append("\n");
        sb.append(replaceFormat("import {}.dao.{}Dao;\n", 0, replaceCount++));
        sb.append(replaceFormat("import {}.mapper.{}Mapper;\n", 0, 1));
        sb.append(replaceFormat("import {}.*;\n", replaceCount++));
        sb.append("import org.slf4j.Logger;\n");
        sb.append("import org.slf4j.LoggerFactory;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.beans.factory.annotation.Qualifier;\n");
        sb.append("import org.springframework.stereotype.Repository;\n");
        sb.append("\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.Map;\n");
        sb.append("\n");
        sb.append(getCopyRightString(replaceCount++));
        sb.append("@Repository\n");
        sb.append(replaceFormat("public class {}DaoImpl implements {}Dao {\n", 1, 1));
        sb.append(replaceFormat("private static final Logger log = LoggerFactory.getLogger({}DaoImpl.class);\n", 1));
        sb.append("\n");
        sb.append("\t@Autowired\n");
        sb.append(replaceFormat("\t@Qualifier(\"{}Mapper\")\n", replaceCount++));
        sb.append(replaceFormat("\tprivate {}Mapper {}Mapper;\n", 1, 4));
        sb.append("\n");
        sb.append("}\n");
        String daoImplNewString = replaceAll(sb,
                configuration.defaultPackageNamePrefix,
                configuration.upperFunctionName,
                getPackageName("proto"),
                getTime(),
                configuration.functionName);
        log.info("daoImplNewString:\n{}", daoImplNewString);
        return daoImplNewString;
    }

    private String getDaoImplAppendString() {
        int replaceCount = 0;
        String daoImplAppendString = null;
        StringBuilder sb = new StringBuilder();
        sb.append("\t@Override\n");
        if (pageEnabled() || isReturnResult()) {
            sb.append(replaceFormat("\tpublic List<{}Record.Builder> {}(Map<String, Object> ins) {\n", replaceCount++, replaceCount++));
            sb.append(replaceFormat("\t\treturn {}Mapper.{}(ins);\n", replaceCount++, 1));
            sb.append("\t}\n");
            daoImplAppendString = replaceAll(sb,
                    configuration.upperMethodName,
                    configuration.methodName,
                    configuration.functionName);
        } else {
            sb.append(replaceFormat("public void {}(Map<String, Object> ins) {\n", replaceCount++));
            sb.append(replaceFormat("\t\t{}Mapper.{}(ins);\n", replaceCount++, 0));
            sb.append("\t}\n");
            daoImplAppendString = replaceAll(sb,
                    configuration.methodName,
                    configuration.functionName);
        }
        log.info("daoImplAppendString:\n{}", daoImplAppendString);
        return daoImplAppendString;
    }

    private String getMapperNewString() {
        int replaceCount = 0;
        String mapperNewString = null;
        StringBuilder sb = new StringBuilder();
        sb.append(replaceFormat("package {}.mapper;\n", replaceCount++));
        sb.append("\n");
        sb.append(replaceFormat("import {}.*;\n", replaceCount++));
        sb.append("import org.apache.ibatis.annotations.Mapper;\n");
        sb.append("import org.apache.ibatis.annotations.Param;\n");
        sb.append("import org.springframework.stereotype.Component;\n");
        sb.append("\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.Map;\n");
        sb.append("\n");
        sb.append(getCopyRightString(replaceCount++));
        sb.append("@Mapper\n");
        sb.append("@Repository\n");
        sb.append(replaceFormat("public interface {}Mapper {\n", replaceCount++));
        sb.append("}\n");
        mapperNewString = replaceAll(sb,
                configuration.defaultPackageNamePrefix,
                getPackageName("proto"),
                getTime(),
                configuration.upperFunctionName);
        log.info("mapperNewString:\n{}", mapperNewString);
        return mapperNewString;
    }

    private String getMapperAppendString() {
        int replaceCount = 0;
        String mapperAppendString = null;
        StringBuilder sb = new StringBuilder();
        if (pageEnabled() || isReturnResult()) {
            sb.append(replaceFormat("\tList<{}Record.Builder> {}(Map<String, Object> ins);\n", replaceCount++, replaceCount));
            mapperAppendString = replaceAll(sb, configuration.upperMethodName, configuration.methodName);
        } else {
            sb.append(replaceFormat("\tvoid {}(Map<String, Object> ins);\n", replaceCount));
            mapperAppendString = replaceAll(sb, configuration.methodName);
        }
        log.info("mapperAppendString:\n{}", mapperAppendString);
        return mapperAppendString;
    }

    private String getMapperXmlNewString() {
        int replaceCount = 0;
        String mapperXmlNewString = null;
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        sb.append("\n");
        sb.append(replaceFormat("<mapper namespace=\"{}.mapper.{}Mapper\">\n", replaceCount++, replaceCount));
        sb.append("</mapper>\n");
        mapperXmlNewString = replaceAll(sb, configuration.defaultPackageNamePrefix, configuration.upperFunctionName);
        log.info("mapperXmlNewString:\n{}", mapperXmlNewString);
        return mapperXmlNewString;
    }

    private String getMapperXmlAppendString() {
        int replaceCount = 0;
        String mapperXmlAppendString = null;
        StringBuilder sb = new StringBuilder();
        // parameterMap
        sb.append(replaceFormat("\t<parameterMap type=\"java.util.Map\" id=\"{}Map\">\n", replaceCount++));
        // 默认的code和note
        sb.append("\t\t<parameter javaType=\"java.lang.Integer\" property=\"O_CODE\" mode=\"OUT\" jdbcType=\"INTEGER\"/>\n");
        sb.append("\t\t<parameter javaType=\"java.lang.String\" property=\"O_NOTE\" mode=\"OUT\" jdbcType=\"VARCHAR\"/>\n");
        if (pageEnabled() || isReturnResult()) {
            sb.append(replaceFormat("\t\t<parameter javaType=\"ResultSet\" property=\"O_RESULT\" mode=\"OUT\" jdbcType=\"CURSOR\" resultMap=\"{}Result\"/>\n", 0));
        }
        if (pageEnabled()) {
            sb.append("\t\t<parameter property=\"I_PAGING\" mode=\"IN\" jdbcType=\"INTEGER\"/>\n");
            sb.append("\t\t<parameter property=\"I_PAGENO\" mode=\"IN\" jdbcType=\"INTEGER\"/>\n");
            sb.append("\t\t<parameter property=\"I_PAGELENGTH\" mode=\"IN\" jdbcType=\"INTEGER\"/>\n");
            sb.append("\t\t<parameter property=\"I_TOTALROWS\" mode=\"INOUT\" jdbcType=\"INTEGER\"/>\n");
            sb.append("\t\t<parameter property=\"I_SORT\" mode=\"IN\" jdbcType=\"VARCHAR\"/>\n");
        }
        // 默认的czr
        sb.append("\t\t<parameter property=\"I_CZR\" mode=\"IN\"/>\n");
        sb.append(getMapperXmlBody(inList));
        sb.append("\t</parameterMap>\n");

        // resultMap
        if (pageEnabled() || isReturnResult()) {
            sb.append(replaceFormat("\t<resultMap id=\"{}Result\" type=\"{}.{}Record$Builder\">\n", 0, replaceCount++, replaceCount++));
            sb.append(getMapperXmlResultBody(outList));
            sb.append("\t</resultMap>\n");
        }

        // 存储过程调用
        sb.append(replaceFormat("\t<select id=\"{}\" statementType=\"CALLABLE\" parameterMap=\"{}Map\">\n", 0, 0));
        sb.append(replaceFormat("\t\tCALL {}({})\n", replaceCount++, replaceCount));
        sb.append("\t</select>\n");
        mapperXmlAppendString = replaceAll(sb,
                configuration.methodName,
                getPackageName("proto"),
                configuration.upperMethodName,
                configuration.procedureName,
                getMapperXmlProcedureParams());
        log.info("mapperXmlAppendString:\n{}", mapperXmlAppendString);
        return mapperXmlAppendString;
    }

    private String getMapperXmlProcedureParams() {
        StringBuilder sb = new StringBuilder();
        // code note
        sb.append("?,?");
        // result
        if (pageEnabled() || isReturnResult()) {
            sb.append(",?");
        }
        // 分页
        if (pageEnabled()) {
            sb.append(",?,?,?,?,?");
        }
        // czr
        sb.append(",?");
        // 入参
        for (ImportDataModel importDataModel : inList) {
            if (importDataModel.isList() || importDataModel.getName().matches(configuration.getWhiteRegExp())) {
                continue;
            }
            sb.append(",?");
        }
        return sb.toString();
    }

    private String getMapperXmlResultBody(List<ImportDataModel> outList) {
        StringBuilder sb = new StringBuilder();
        for (ImportDataModel importDataModel : outList) {
            if (importDataModel.isList() || importDataModel.getName().equals("code") || importDataModel.getName().equals("note")) {
                continue;
            }
            sb.append(replaceAll("\t\t<result column=\"{0}\" property=\"{1}\"/>\n",
                    importDataModel.getProcedureParam(), importDataModel.getName()));
        }
        return sb.toString();
    }

    private String getMapperXmlBody(List<ImportDataModel> importDataModelList) {
        StringBuilder sb = new StringBuilder();
        for (ImportDataModel importDataModel : importDataModelList) {
            if (importDataModel.isList() ||
                    (StringUtils.isNotBlank(configuration.getWhiteRegExp())
                            && importDataModel.getName().matches(configuration.getWhiteRegExp()))) {
                continue;
            }
            sb.append(replaceAll("\t\t<parameter property=\"{0}\" mode=\"IN\"/>\n", importDataModel.getProcedureParam()));
        }
        return sb.toString();
    }

    private String getClientTestNewString() {
        int replaceCount = 0;
        String clientTestNewString = null;
        StringBuilder sb = new StringBuilder();
        sb.append(replaceFormat("package {};\n", replaceCount++));
        sb.append("\n");
        sb.append("import com.alibaba.fastjson.JSON;\n");
        sb.append("import com.alibaba.fastjson.JSONObject;\n");
        sb.append("import com.alibaba.fastjson.serializer.SerializerFeature;\n");
        sb.append("import com.apex.ams.annotation.AmsBlockingStub;\n");
        sb.append(replaceFormat("import com.apexsoft.crm.{}ServerApplication;\n", replaceCount++));
        sb.append("import com.apexsoft.utils.FileUtils;\n");
        sb.append("import com.apexsoft.utils.ProtoBufUtil;\n");
        sb.append(replaceFormat("import {}.*;\n", replaceCount++));
        sb.append("\n");
        sb.append("import org.hamcrest.Matchers;\n");
        sb.append("import org.junit.Assert;\n");
        sb.append("import org.junit.Before;\n");
        sb.append("import org.junit.Test;\n");
        sb.append("import org.junit.runner.RunWith;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.boot.test.context.SpringBootTest;\n");
        sb.append("import org.springframework.http.HttpEntity;\n");
        sb.append("import org.springframework.http.HttpHeaders;\n");
        sb.append("import org.springframework.http.MediaType;\n");
        sb.append("import org.springframework.http.ResponseEntity;\n");
        sb.append("import org.springframework.test.context.junit4.SpringRunner;\n");
        sb.append("import org.springframework.web.client.RestTemplate;\n");
        sb.append("\n");
        sb.append("import java.util.Arrays;\n");
        sb.append("import java.util.HashMap;\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.Map;\n");
        sb.append("\n");
        sb.append("@RunWith(SpringRunner.class)\n");
        sb.append(replaceFormat("@SpringBootTest(classes = {}ServerApplication.class)\n", 1));
        sb.append(replaceFormat("public class {}ClientTest {\n", replaceCount++));
        sb.append("\tpublic static final String LOCALHOST = \"http://localhost:8000/org\";\n");
        sb.append("\tpublic static final String REMOTE = \"http://192.168.24.252:8001/org\";\n");
        sb.append("\tpublic static final String ACTIVE = LOCALHOST;\n");
        sb.append("\n");
        sb.append("\t@Autowired\n");
        sb.append("\tprivate RestTemplate restTemplate;\n");
        sb.append("\n");
        sb.append("\tprivate List<String> cookies = Arrays.asList(\"LBSESSION=YWUzNDdkYzEtY2U5MS00ODgxLWE2YzYtYjFkNzdjMzdiM2Qz; Path=/; HttpOnly\");\n");
        sb.append("\n");
        sb.append("\t@Test\n");
        sb.append("\tpublic void testContextLoads() {\n");
        sb.append("\t\tSystem.out.println(restTemplate);\n");
        sb.append("\t}\n");
        sb.append("\n");
        sb.append("\t@Before\n");
        sb.append("\t@Test\n");
        sb.append("\tpublic void auth() {\n");
        sb.append("\t\tHttpHeaders headers = new HttpHeaders();\n");
        sb.append("\t\theaders.setContentType(MediaType.APPLICATION_JSON);\n");
        sb.append("\t\tString json = \"{\\n\" +\n");
        sb.append("\t\t\t\t\"  \\\"clientId\\\": \\\"c5::pc\\\",\\n\" +\n");
        sb.append("\t\t\t\t\"  \\\"signature\\\": \\\"aElUNn15fKgHT2MY/KQL+2PMhAaKyUVCFONZud2gtW53r5zhYdDwh5toXzBZUR4zZR/Q/XPGtksax+hQ6HBT4r9wLh+RUEyjEXn93zMQnGBcRTR46JoiFhHzpScdCZLX\\\",\\n\" +\n");
        sb.append("\t\t\t\t\"  \\\"ext\\\": \\\"\\\"\\n\" +\n");
        sb.append("\t\t\t\t\"}\";\n");
        sb.append("\t\tHttpEntity<String> httpEntity = new HttpEntity<>(json, headers);\n");
        sb.append("\t\tResponseEntity<JSONObject> entity = restTemplate.postForEntity(ACTIVE + \"/auth\", httpEntity, JSONObject.class);\n");
        sb.append("\t\tthis.cookies = entity.getHeaders().get(\"Set-Cookie\");\n");
        sb.append("\t}\n");
        sb.append("\n");
        sb.append("\tprivate void assert0(ResponseEntity<JSONObject> entity) {\n");
        sb.append("\t\tAssert.assertNotNull(entity);\n");
        sb.append("\t\tAssert.assertNotNull(entity.getBody());\n");
        sb.append("\t\tAssert.assertNotNull(entity.getBody().get(\"code\"));\n");
        sb.append("\t\tSystem.out.println(JSON.toJSONString(entity.getBody(), SerializerFeature.PrettyFormat));\n");
        sb.append("\t\tAssert.assertThat(Integer.parseInt(String.valueOf(entity.getBody().get(\"code\"))), Matchers.greaterThanOrEqualTo(0));\n");
        sb.append("\t}\n");
        sb.append("\n");
        sb.append("}\n");
        clientTestNewString = replaceAll(sb,
                configuration.defaultPackageNamePrefix,
                configuration.upperServiceName,
                getPackageName("proto"),
                configuration.upperFunctionName,
                configuration.functionName);
        log.info("clientTestNewString:\n{}", clientTestNewString);
        return clientTestNewString;
    }

    private String getClientTestAppendString() {
        int replaceCount = 0;
        String clientTestAppendString = null;
        StringBuilder sb = new StringBuilder();
        sb.append("\t@Test\n");
        sb.append(replaceFormat("\tpublic void {}() {\n", replaceCount++));
        sb.append("\t\tHttpHeaders headers = new HttpHeaders();\n");
        sb.append("\t\theaders.put(HttpHeaders.COOKIE, cookies);\n");
        sb.append("\t\theaders.setContentType(MediaType.APPLICATION_JSON);\n");
        sb.append(replaceFormat("\t\tHttpEntity<String> httpEntity = new HttpEntity<>(FileUtils.readString({}ClientTest.class, \"{}\"), headers);\n", replaceCount++, replaceCount++));
        sb.append(replaceFormat("\t\tResponseEntity<JSONObject> entity = restTemplate.postForEntity(ACTIVE + \"/{}/{}/v1/{}\", httpEntity, JSONObject.class);\n", replaceCount++, replaceCount++, replaceCount++));
        sb.append("\t\tassert0(entity);\n");
        sb.append("\t}\n");
        clientTestAppendString = replaceAll(sb,
                configuration.methodName,
                configuration.upperFunctionName,
                configuration.interfaceIndex + "_" + configuration.methodName,
                configuration.serviceName,
                configuration.functionName,
                configuration.methodName);
        log.info("clientTestAppendString:\n{}", clientTestAppendString);
        return clientTestAppendString;
    }

    private String getServerTestNewString() {
        int replaceCount = 0;
        String serverTestNewString = null;
        StringBuilder sb = new StringBuilder();
        sb.append(replaceFormat("package {};\n", replaceCount++));
        sb.append("\n");
        sb.append("import com.alibaba.fastjson.JSON;\n");
        sb.append("import com.alibaba.fastjson.JSONObject;\n");
        sb.append("import com.alibaba.fastjson.serializer.SerializerFeature;\n");
        sb.append("import com.apex.ams.annotation.AmsBlockingStub;\n");
        sb.append(replaceFormat("import com.apexsoft.crm.{}ServerApplication;\n", replaceCount++));
        sb.append("import com.apexsoft.utils.FileUtils;\n");
        sb.append("import com.apexsoft.utils.ProtoBufUtil;\n");
        sb.append(replaceFormat("import {}.*;\n", replaceCount++));
        sb.append("\n");
        sb.append("import org.hamcrest.Matchers;\n");
        sb.append("import org.junit.Assert;\n");
        sb.append("import org.junit.Before;\n");
        sb.append("import org.junit.Test;\n");
        sb.append("import org.junit.runner.RunWith;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.boot.test.context.SpringBootTest;\n");
        sb.append("import org.springframework.http.HttpEntity;\n");
        sb.append("import org.springframework.http.HttpHeaders;\n");
        sb.append("import org.springframework.http.MediaType;\n");
        sb.append("import org.springframework.http.ResponseEntity;\n");
        sb.append("import org.springframework.test.context.junit4.SpringRunner;\n");
        sb.append("import org.springframework.web.client.RestTemplate;\n");
        sb.append("\n");
        sb.append("import java.util.Arrays;\n");
        sb.append("import java.util.HashMap;\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.Map;\n");
        sb.append("\n");
        sb.append("@RunWith(SpringRunner.class)\n");
        sb.append(replaceFormat("@SpringBootTest(classes = {}ServerApplication.class)\n", 1));
        sb.append(replaceFormat("public class {}ServerTest {\n", replaceCount++));
        sb.append("\n");
        sb.append("\t@AmsBlockingStub\n");
        sb.append(replaceFormat("\tprivate {}ServiceGrpc.{}ServiceBlockingStub {}ServiceBlockingStub;\n", 3, 3, replaceCount++));
        sb.append("\n");
        sb.append("}\n");
        serverTestNewString = replaceAll(sb,
                configuration.defaultPackageNamePrefix,
                configuration.upperServiceName,
                getPackageName("proto"),
                configuration.upperFunctionName,
                configuration.functionName);
        log.info("serverTestNewString:\n{}", serverTestNewString);
        return serverTestNewString;
    }

    private String getServerTestAppendString() {
        int replaceCount = 0;
        String serverTestAppendString = null;
        StringBuilder sb = new StringBuilder();
        sb.append("\t@Test\n");
        sb.append(replaceFormat("\tpublic void {}() {\n", replaceCount++));
        sb.append(replaceFormat("\t\t{}Req.Builder builder =\n", replaceCount++));
        sb.append(replaceFormat("\t\t\t\t({}Req.Builder) ProtoBufUtil.parseFile({}ServerTest.class,\n", 1, replaceCount++));
        sb.append(replaceFormat("\t\t\t\t\t\t\t\t\"{}\", {}Req.newBuilder());\n", replaceCount++, 1));
        sb.append(replaceFormat("\t\t{}Rsp rsp = {}ServiceBlockingStub.{}(builder.build());\n", 1, replaceCount++, 0));
        sb.append("\t\tProtoBufUtil.print(rsp);\n");
        sb.append("\t\tAssert.assertNotNull(rsp);\n");
        sb.append("\t\tAssert.assertThat(rsp.getCode(), Matchers.greaterThanOrEqualTo(0));\n");
        sb.append("\t\tAssert.assertEquals(rsp.getNote(), \"成功\");\n");
        sb.append("\t}\n");
        serverTestAppendString = replaceAll(sb,
                configuration.methodName,
                configuration.upperMethodName,
                configuration.upperFunctionName,
                configuration.interfaceIndex + "_" + configuration.methodName,
                configuration.functionName);
        log.info("serverTestAppendString:\n{}", serverTestAppendString);
        return serverTestAppendString;
    }

    private String getTestDataNewString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        // 默认的czr
        sb.append("\t\"czr\": \"0\"").append(",").append("\n");
        sb.append(getTestDataBody(inList));

        // 删除最好一个逗号
        deleteEnd(sb);
        deleteEnd(sb);
        sb.append("\n");

        sb.append("}\n");
        return sb.toString();
    }

    private String getTestDataBody(List<ImportDataModel> inList) {
        StringBuilder sb = new StringBuilder();
        for (ImportDataModel importDataModel : inList) {
            if (importDataModel.isList() || importDataModel.getName().matches(configuration.getWhiteRegExp())) {
                continue;
            }
            sb.append(replaceAll("\t\"{0}\": \"{1}\",\n", importDataModel.getName(), ""));
        }
        return sb.toString();
    }
}
