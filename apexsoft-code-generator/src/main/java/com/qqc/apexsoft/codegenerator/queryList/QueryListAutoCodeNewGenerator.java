package com.qqc.apexsoft.codegenerator.queryList;

import com.qqc.apexsoft.codegenerator.common.AutoCodeGenerator;
import com.qqc.apexsoft.codegenerator.common.BasicAutoCodeGenerator;
import com.qqc.apexsoft.codegenerator.constants.ItemTypeEnum;
import com.qqc.apexsoft.codegenerator.model.ImportDataModel;
import com.qqc.apexsoft.codegenerator.model.ImportDataType;
import com.qqc.apexsoft.codegenerator.utils.AutoCodeGeneratorException;
import com.qqc.apexsoft.codegenerator.utils.AutoCodeGeneratorHelper;
import com.qqc.apexsoft.codegenerator.utils.FileUtils;
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
import java.util.Map;
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

    @Override
    public void autoCodeGenerate() {
        AutoCodeGenerator.super.autoCodeGenerate();
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
        write3(getPath(mainName), getTestDataNewString());
    }

    @Override
    public void afterPropertiesSet() {
        fileCheck();
        // 组装模板代码变量Map
        assemFieldNameMap();
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

    private String getConsumerAppendString() {
        if (configuration.enabledNewVersion) {
            return transform(ItemTypeEnum.Consumer_Append.getItemName());
        }
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

    private String transformFromProviderToXml() {
        StringBuilder sb = new StringBuilder();
        // 中间插入语句的第一行不需要通过tab来调整格式
        sb.append("Map<String, Object> ins = new HashMap<>();\n");
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
        // 去除最后多余的换行符
        deleteEnd(sb);
        return sb.toString();
    }

    private String getProviderAppendString() {
        if (configuration.enabledNewVersion) {
            // 构建调用存储过程的map
            fieldNameMap.put("transformFromProviderToXml", transformFromProviderToXml());
            // 判断是否分页和是否返回结果集
            ItemTypeEnum itemTypeEnum = null;
            if (pageEnabled()) {
                itemTypeEnum = ItemTypeEnum.Provider_Append_Page;
            } else if (isReturnResult()) {
                itemTypeEnum = ItemTypeEnum.Provider_Append_Result;
            } else {
                itemTypeEnum = ItemTypeEnum.Provider_Append_No_Result;
            }
            return transform(itemTypeEnum.getItemName());
        }

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

    private String getDaoAppendString() {
        if (configuration.enabledNewVersion) {
            ItemTypeEnum itemTypeEnum = null;
            if (pageEnabled() || isReturnResult()) {
                itemTypeEnum = ItemTypeEnum.Dao_Append_Page_Or_Result;
            } else {
                itemTypeEnum = ItemTypeEnum.Dao_Append_No_Result;
            }
            return transform(itemTypeEnum.getItemName());
        }
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

    private String getDaoImplAppendString() {
        if (configuration.enabledNewVersion) {
            ItemTypeEnum itemTypeEnum = null;
            if (pageEnabled() || isReturnResult()) {
                itemTypeEnum = ItemTypeEnum.DaoImpl_Append_Page_Or_Result;
            } else {
                itemTypeEnum = ItemTypeEnum.DaoImpl_Append_No_Result;
            }
            return transform(itemTypeEnum.getItemName());
        }
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

    private String getMapperAppendString() {
        if (configuration.enabledNewVersion) {
            ItemTypeEnum itemTypeEnum = null;
            if (pageEnabled() || isReturnResult()) {
                itemTypeEnum = ItemTypeEnum.Mapper_Append_Page_Or_Result;
            } else {
                itemTypeEnum = ItemTypeEnum.Mapper_Append_No_Result;
            }
            return transform(itemTypeEnum.getItemName());
        }
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

    private String getMapperXmlAppendString() {
        int replaceCount = 0;
        String mapperXmlAppendString = null;
        StringBuilder sb = new StringBuilder();
        // parameterMap
        sb.append(replaceFormat("\t<parameterMap type=\"java.util.Map\" id=\"{}Map\">\n", replaceCount++));
        // 默认的code和note
        sb.append("\t\t<parameter javaType=\"java.lang.Integer\" property=\"O_CODE\" mode=\"OUT\" jdbcType=\"INTEGER\"/>\n");
        sb.append("\t\t<parameter javaType=\"java.lang.String\" property=\"O_NOTE\" mode=\"OUT\" jdbcType=\"VARCHAR\"/>\n");
        if (pageEnabled()) {
            sb.append("\t\t<parameter javaType=\"java.lang.Integer\" property=\"O_HASRECORDSET\" mode=\"OUT\" jdbcType=\"INTEGER\"/>\n");
        }
        if (isReturnResult()) {
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
            sb.append(",?,?,?,?,?,?");
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

    private void assemFieldNameMap() {
        String methodName = configuration.methodName;//editExistingCustomers
        fieldNameMap.put("methodName", methodName);
        fieldNameMap.put("methodDesc", configuration.methodDesc);

        // 需要计算的fieldName
        String upperMethodName = upper(methodName);//EditExistingCustomers
        String rspName = upperMethodName + "Rsp";//EditExistingCustomersRsp
        fieldNameMap.put("rspName", rspName);
        String reqName = upperMethodName + "Req";//EditExistingCustomersReq
        fieldNameMap.put("reqName", reqName);
        String requestModelName = upperMethodName + "Model";//EditExistingCustomersModel
        fieldNameMap.put("requestModelName", requestModelName);
        String recordName = upperMethodName + "Record";
        fieldNameMap.put("recordName", recordName);

        // 配置文件中的fieldName
        fieldNameMap.put("controllerName", configuration.controllerName);
        fieldNameMap.put("consumerName", configuration.consumerName);
        fieldNameMap.put("consumerVariableName", configuration.consumerVariableName);
        fieldNameMap.put("requestModelVariableName", configuration.requestModelVariableName);
        fieldNameMap.put("blockingStubVariableName", configuration.blockingStubVariableName);
        fieldNameMap.put("providerName", configuration.providerName);
        fieldNameMap.put("daoVariableName", configuration.daoVariableName);
        fieldNameMap.put("mapperVariableName", configuration.mapperVariableName);
    }
}
