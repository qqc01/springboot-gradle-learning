package com.qqc.apexsoft.codegenerator.common;

import com.alibaba.excel.EasyExcel;
import com.google.common.collect.Maps;
import com.qqc.apexsoft.codegenerator.config.CodeGeneratorConfiguration;
import com.qqc.apexsoft.codegenerator.constants.ItemTypeEnum;
import com.qqc.apexsoft.codegenerator.model.ImportDataModel;
import com.qqc.apexsoft.codegenerator.model.ImportDataType;
import com.qqc.apexsoft.codegenerator.utils.AutoCodeGeneratorException;
import com.qqc.apexsoft.codegenerator.utils.ExcelListener;
import com.qqc.apexsoft.codegenerator.utils.FileUtils;
import com.qqc.apexsoft.codegenerator.utils.StringHelper;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Component
public class BasicAutoCodeGenerator {
    private static final Logger log = LoggerFactory.getLogger(BasicAutoCodeGenerator.class);
    @Autowired
    protected CodeGeneratorConfiguration configuration;
    /**
     * list索引，起始为2
     */
    private int index = 2;

    /**
     * 字段索引，默认为1
     */
    protected int fieldIndex = 1;
    /**
     * 基准类
     */
    protected static Class<?> basicClass;
    /**
     * 是否匹配到RPC
     */
    private boolean matchedRPC = false;
    private StringHelper stringHelper = StringHelper.getInstance();
    private String path;
    private List<String> originData;//原始数据
    protected List<ImportDataModel> inList;
    protected List<ImportDataModel> outList;
    /**
     * 主名称
     */
    protected String mainName;

    public String modelName;

    public String modelDesc;


    /**
     * 模板代码变量Map
     */
    protected Map<String, String> fieldNameMap = Maps.newHashMap();


    protected BasicAutoCodeGenerator() {
        config();
    }

    protected String upper(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return String.valueOf(str.charAt(0)).toUpperCase().concat(str.substring(1));
    }

    protected List<Map<Integer, String>> readExcel(int sheetNo, Class<?> clazz) {
        ExcelListener<Map<Integer, String>> listener = new ExcelListener<>();
        InputStream is = clazz.getResourceAsStream("generateCode.xlsx");
        EasyExcel.read(is, listener).sheet(sheetNo).doRead();
        return listener.getData();
    }

    protected List<ImportDataModel> getAllList() {
        List<ImportDataModel> list = new ArrayList<>();
        list.addAll(inList);
        list.addAll(outList);
        return list.stream().filter(ImportDataModel::isList).collect(Collectors.toList());
    }

    protected Class<?> getBasicClass() {
        return basicClass;
    }

    protected static void setBasicClass(Class<?> basicClass) {
        BasicAutoCodeGenerator.basicClass = basicClass;
    }


    public void config() {

    }

    protected boolean isList(Object obj) {
        return false;
    }

    protected String replaceAll(StringBuilder src, String... values) {
        String result = src.toString();
        for (int i = 0; i < values.length; i++) {
            result = result.replaceAll("\\{" + i + "}", values[i]);
        }
        return result;
    }

    protected String replaceAll(String src, Object... values) {
        String result = src;
        for (int i = 0; i < values.length; i++) {
            result = result.replaceAll("\\{" + i + "}", String.valueOf(values[i]));
        }
        return result;
    }

    public String replaceFormat(String src, Integer... replaceCounts) {
        StringBuilder sb = new StringBuilder();
        char[] chars = src.toCharArray();
        List<Integer> replaceCountList = new ArrayList<>();
        Collections.addAll(replaceCountList, replaceCounts);
        Iterator<Integer> iterator = replaceCountList.iterator();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '{' && chars[i + 1] == '}') {
                sb.append(chars[i]).append(iterator.next());
            } else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }

    protected List<ImportDataModel> getDataList(int sheetNo) {
        List<ImportDataModel> dataList = new ArrayList<>();
        for (Map<Integer, String> map : readExcel(sheetNo, getBasicClass())) {
            String name = map.get(3);//第四列
            String desc = map.get(0);//第一列
            String listObjName = upper(map.get(4));
            ImportDataModel importDataModel = new ImportDataModel();
            importDataModel.setName(name);
            importDataModel.setDesc(desc);
            importDataModel.setProcedureParam(map.get(1));
            importDataModel.setType(ImportDataType.COMMON);
            importDataModel.setRow(map);
            if (isList(name)) {
                importDataModel.setListData(getDataList(index++));
                importDataModel.setType(ImportDataType.LIST);
                importDataModel.setListObjName(listObjName);
            }
            dataList.add(importDataModel);
        }
        return dataList;
    }

    protected void deleteEnd(StringBuilder sb) {
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    private void write(String context) {
        BufferedWriter bw = null;
        String line = null;
        try {
            bw = new BufferedWriter(new FileWriter(path));
            if (originData != null && originData.size() > 0) {
                Iterator<String> iterator = originData.iterator();
                line = iterator.next();
                while (iterator.hasNext()) {
                    bw.write(line);
                    bw.write("\n");
                    line = iterator.next();
                }
//                bw.write("\n");
            }

            // 开始写新代码
            StringBuilder sb = new StringBuilder();
            if (configuration.getInterfaceIndex() != 1) {
                sb.append("\n");
            }
            sb.append(context);
            sb.append(line == null ? "" : line);
            bw.write(sb.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write4(String context) {
        BufferedWriter bw = null;
        String line = null;
        try {
            bw = new BufferedWriter(new FileWriter(path));
            if (originData != null && originData.size() > 0) {
                Iterator<String> iterator = originData.iterator();
                line = iterator.next();
                while (iterator.hasNext()) {
                    bw.write(line);
                    bw.write("\n");
                    line = iterator.next();
                }
//                bw.write("\n");
            }

            // 开始写新代码
            StringBuilder sb = new StringBuilder();
//            if (configuration.getInterfaceIndex() != 1) {
//                sb.append("\n");
//            }
            sb.append(context);
            sb.append(line == null ? "" : line);
            bw.write(sb.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入除proto追加内容之外的所有内容
     * 包括新增内容和追加内容
     *
     * @param path    路径
     * @param context 内容
     */
    protected void write0(String path, String context) {
        setPath(path);
        log.info("setPath:" + path);
        setOriginData();
        write(context);
    }

    /**
     * 写入Model和testData等一直都是新增的内容
     *
     * @param path    路径
     * @param context 内容
     */
    protected void write3(String path, String context) {
        setPath(path);
        log.info("setPath:" + path);
        setOriginData();
        write4(context);
    }

    protected void write1(String context1, String context2) {
        BufferedWriter bw = null;
        try {
            if (originData.size() == 0) {
                throw new AutoCodeGeneratorException("文件内容为空，不允许追加内容！！！");
            } else {
                bw = new BufferedWriter(new FileWriter(path));
                Iterator<String> iterator = originData.iterator();
                String pre = iterator.next();
                String line = null;
                while (iterator.hasNext()) {
                    line = iterator.next();
                    bw.write(pre);
                    bw.write("\r\n");
                    pre = line;
                    if (pre.matches(configuration.getRpcRegExp())) {
                        bw.write(context1);
                        bw.write("\n");
                        matchedRPC = true;
                    }
                }

                if (matchedRPC) {
                    log.info("使用正则[" + configuration.getRpcRegExp() + "]成功匹配到proto Service");
                } else {
                    String msg = "没有匹配到proto Service！！！";
                    throw new AutoCodeGeneratorException(msg);
                }
                // 开始写新代码
                StringBuilder sb = new StringBuilder();
                if (configuration.getInterfaceIndex() != 1) {
                    sb.append("\n");
                }
                sb.append(context2);
                sb.append("}");
                bw.write(sb.toString());
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入proto追加内容
     *
     * @param path     proto路径
     * @param context1 proto 定义
     * @param context2 rpc 定义
     */
    protected void write2(String path, String context1, String context2) {
        setPath(path);
        log.info("setPath:" + path);
        setOriginData();
        write1(context1, context2);
    }

    private void setOriginData() {
        List<String> originDataList = new ArrayList<>();
        if (!new File(path).exists()) {
            creatFile();
            originData = null;
            return;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String context = null;
            while ((context = br.readLine()) != null) {
                originDataList.add(context);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        originData = originDataList;
        log.info("setOriginData:{}", originData);
    }

    private void creatFile() {
        File file = new File(path);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void fileCheck() {
        if (!isCreateFile()) {
            try {
                assertAndLog("proto");
            } catch (Exception e) {
                throw new AutoCodeGeneratorException("请检查proto路径！！！");
            }
            try {
                assertAndLog("controller");
            } catch (Exception e) {
                throw new AutoCodeGeneratorException("请检查controller路径！！！");
            }
            try {
                assertAndLog("consumer");
            } catch (Exception e) {
                throw new AutoCodeGeneratorException("请检查consumer路径！！！");
            }
            try {
                assertAndLog("provider");
            } catch (Exception e) {
                throw new AutoCodeGeneratorException("请检查provider路径！！！");
            }
            try {
                assertAndLog("dao");
            } catch (Exception e) {
                throw new AutoCodeGeneratorException("请检查dao路径！！！");
            }
            try {
                assertAndLog("daoImpl");
            } catch (Exception e) {
                throw new AutoCodeGeneratorException("请检查daoImpl路径！！！");
            }
            try {
                assertAndLog("mapper");
            } catch (Exception e) {
                throw new AutoCodeGeneratorException("请检查mapper路径！！！");
            }
            try {
                assertAndLog("mapperXml");
            } catch (Exception e) {
                throw new AutoCodeGeneratorException("请检查mapperXml路径！！！");
            }
            try {
                assertAndLog("clientTest");
            } catch (Exception e) {
                throw new AutoCodeGeneratorException("请检查clientTest路径！！！");
            }
            try {
                assertAndLog("serverTest");
            } catch (Exception e) {
                throw new AutoCodeGeneratorException("请检查serverTest路径！！！");
            }
        }
        log.info("文件路径检查通过");
    }

    private void assertAndLog(String name) {
        String path = getPath(name);
        // log.info(name + ":" + path);
        log.warn("{}:{}", name, path);
        Assert.isTrue(new File(path).exists());
    }


    public String getPath(String mainName) {
        Assert.notNull(configuration.protoSrcPath);
        Assert.notNull(configuration.clientSrcPath);
        Assert.notNull(configuration.serverSrcPath);
        Assert.notNull(configuration.mapperXmlSrcPath);
        Assert.notNull(configuration.serverTestSrcPath);
        Assert.notNull(configuration.serverTestDataSrcPath);
        Assert.notNull(configuration.defaultPackageNamePrefix);
        Assert.notNull(configuration.upperFunctionName);
        String var0 = null;//根路径
        String var1 = stringHelper.replace(getPackageName(mainName)).getPath();//包路径
        String var2 = null;//文件名
        String var3 = ".java";//文件后缀
        switch (mainName) {
            case "proto":
                var0 = configuration.protoSrcPath;
                var2 = configuration.protoName;
                var3 = ".proto";
                return configuration.protoPath;
            case "model":
                var0 = configuration.clientSrcPath;
                var2 = StringUtils.isBlank(modelName) ? upper(configuration.methodName) + "Model" : modelName;
                break;
            case "controller":
                var0 = configuration.clientSrcPath;
                var2 = configuration.upperFunctionName + "Controller";
                break;
            case "consumer":
                var0 = configuration.clientSrcPath;
                var2 = configuration.upperFunctionName + "Consumer";
                break;
            case "provider":
                var0 = configuration.serverSrcPath;
                var2 = configuration.upperFunctionName + "Provider";
                break;
            case "dao":
                var0 = configuration.serverSrcPath;
                var2 = configuration.upperFunctionName + "Dao";
                break;
            case "daoImpl":
                var0 = configuration.serverSrcPath;
                var2 = configuration.upperFunctionName + "DaoImpl";
                break;
            case "mapper":
                var0 = configuration.serverSrcPath;
                var2 = configuration.upperFunctionName + "Mapper";
                return configuration.mapperPath;
            case "mapperXml":
                var0 = configuration.mapperXmlSrcPath;
                var2 = configuration.upperFunctionName + "Mapper";
                var3 = ".xml";
                return configuration.mapperXmlPath;
            case "clientTest":
                var0 = configuration.serverTestSrcPath;
                var2 = configuration.upperFunctionName + "ClientTest";
                break;
            case "serverTest":
                var0 = configuration.serverTestSrcPath;
                var2 = configuration.upperFunctionName + "ServerTest";
                break;
            case "testData":
                var0 = configuration.serverTestDataSrcPath;
                var2 = configuration.interfaceIndex + "_" + configuration.methodName;
                var3 = "";
                break;
        }
        return stringHelper.concat(var0)
                .concatPath(var1)
                .concatPath(var2)
                .concat(var3)
                .getPath();
    }

    public String getPackageName(String mainName) {
        String packageName = mainName;
        if (mainName.equals("proto")) {
            return configuration.protoPackageName;
        }
        if (mainName.equals("consumer")) {
            packageName = "service";
        }
        if (mainName.equals("mapperXml")) {
            return "";
        }
        if (mainName.equals("daoImpl")) {
            packageName = "dao.impl";
        }
        if (mainName.equals("clientTest") || mainName.equals("serverTest") || mainName.equals("testData")) {
            return configuration.defaultPackageNamePrefix;
        }
        return configuration.defaultPackageNamePrefix.concat(".").concat(packageName);
    }

    protected void writeModel(String path, List<ImportDataModel> list) {
        if (CollectionUtils.isEmpty(list)) {
            throw new AutoCodeGeneratorException("list数据为空！！！");
        }
        write3(path, getModelNewString(list));
        List<ImportDataModel> childList = list.stream().filter(ImportDataModel::isList).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(childList)) {
            for (ImportDataModel importDataModel : childList) {
                setModelName(importDataModel.getListObjName());
                setModelDesc(importDataModel.getDesc());
                writeModel(getPath("model"), importDataModel.getListData());
            }
        }
    }

    protected String getModelNewString(List<ImportDataModel> importDataModelList) {
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
//        sb.append("\n");
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

    protected String getModelGetterAndSetter(List<ImportDataModel> list) {
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

    protected String getProtoNewString() {
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

    protected String getProtoBody(List<ImportDataModel> list) {
        return getProtoBody(list, configuration.getWhiteRegExp());
    }

    protected String getProtoBody(List<ImportDataModel> list, String whiteRegExp) {
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

    protected String getProtoRPCAppendString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t//{0}\n").append("\trpc {1} ({2}Req) returns ({2}Rsp) {};\n");
        String protoRPCAppendString = replaceAll(sb, configuration.methodDesc, configuration.methodName, configuration.upperMethodName);
        log.info("构建protoRPCAppendString:\n{}", protoRPCAppendString);
        return protoRPCAppendString;
    }

    protected String getControllerAppendString() {
        if (configuration.enabledNewVersion) {
            return transform(ItemTypeEnum.Controller_Append.getItemName());
        }

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

    protected String getControllerNewString() {
        int replaceCount = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(replaceFormat("package {};\n", replaceCount++));
        sb.append("\n");
        sb.append("import com.apexsoft.crm.ams.annotation.ApiPost;\n");
        sb.append(replaceFormat("import {}.*;\n", replaceCount++));
        sb.append(replaceFormat("import {}.{}Consumer;\n", replaceCount++, replaceCount++));
        sb.append("import com.apexsoft.crm.constant.CustomerRequestCommonPathConstant;\n");
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
        sb.append(replaceFormat("@RequestMapping(value = CustomerRequestCommonPathConstant.REQUEST_PROJECT_PATH + \"/{}\")\n", replaceCount++));
        sb.append(replaceFormat("@Api(tags = \"{}\")\n", replaceCount++));
        sb.append(replaceFormat("public class {}Controller_Append {\n", 3));
        sb.append(replaceFormat("\tprivate static final Logger log = LoggerFactory.getLogger({}Controller_Append.class);\n", 3));
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

    protected String getConsumerNewString() {
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
        sb.append("import org.springframework.stereotype.Service;\n");
        sb.append("\n");
        sb.append("import javax.annotation.Resource;\n");
        sb.append("import java.io.ByteArrayInputStream;\n");
        sb.append("import java.io.InputStream;\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.Map;\n");
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
        sb.append("\n");
        sb.append("\t@Resource\n");
        sb.append("\tprivate FileStorageService fileStorageService;\n");
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

    protected String getProviderNewString() {
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
        sb.append("import java.util.HashMap;\n");
        sb.append("import java.util.List;\n");
        sb.append("import java.util.Map;\n");
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

    protected String getDaoNewString() {
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

    protected String getDaoImplNewString() {
        int replaceCount = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(replaceFormat("package {}.dao.impl;\n", replaceCount++));
        sb.append("\n");
        sb.append(replaceFormat("import {}.dao.{}Dao;\n", 0, replaceCount++));
        sb.append(replaceFormat("import {}.mapper.{}Mapper;\n", 0, 1));
        sb.append(replaceFormat("import {}.*;\n", replaceCount++));
        sb.append("import org.apache.commons.collections.CollectionUtils;\n");
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

    protected String getMapperNewString() {
        int replaceCount = 0;
        String mapperNewString = null;
        StringBuilder sb = new StringBuilder();
        sb.append(replaceFormat("package {}.mapper;\n", replaceCount++));
        sb.append("\n");
        sb.append(replaceFormat("import {}.*;\n", replaceCount++));
        sb.append("import org.apache.ibatis.annotations.Mapper;\n");
        sb.append("import org.apache.ibatis.annotations.Param;\n");
        sb.append("import org.springframework.stereotype.Component;\n");
        sb.append("import org.springframework.stereotype.Repository;\n");
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

    protected String getMapperXmlNewString() {
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

    protected boolean isGet() {
        return false;
    }

    protected String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format(new Date());
    }

    protected String getCopyRightString(int replaceCount) {
        StringBuilder sb = new StringBuilder();
        sb.append("/**\n");
        sb.append(" * @author qqc\n");
        sb.append(replaceFormat(" * @create {}\n", replaceCount));
        sb.append(" * @description\n");
        sb.append(" */\n");
        return sb.toString();
    }

    public boolean isCreateFile() {
        return configuration.isCreateFile;
    }

    public boolean isReturnResult() {
        return configuration.isReturnResult;
    }

    public boolean pageEnabled() {
        return configuration.pageEnabled;
    }

    protected String getConsumerName() {
        return configuration.upperFunctionName + "Consumer";
    }

    protected String getServiceName() {
        return configuration.upperFunctionName + "Service";
    }

    protected String getModelName() {
        if (StringUtils.isBlank(modelName)) {
            return configuration.upperMethodName + "Model";
        }
        return modelName;
    }

    public String getModelDesc() {
        if (StringUtils.isBlank(modelDesc)) {
            return configuration.methodDesc;
        }
        return modelDesc;
    }

    protected void setModelDesc(String desc) {
        this.modelDesc = desc;
    }

    protected String transform(String templateName) {
        String template = FileUtils.readString(this.getClass(), templateName);
        for (Map.Entry<String, String> entry : fieldNameMap.entrySet()) {
            template = template.replaceAll(entry.getKey(), entry.getValue());
        }
        return template;
    }
}
