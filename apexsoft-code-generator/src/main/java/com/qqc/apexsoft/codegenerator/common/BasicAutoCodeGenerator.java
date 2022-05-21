package com.qqc.apexsoft.codegenerator.common;

import com.alibaba.excel.EasyExcel;
import com.qqc.apexsoft.codegenerator.model.ImportDataModel;
import com.qqc.apexsoft.codegenerator.model.ImportDataType;
import com.qqc.apexsoft.codegenerator.utils.AutoCodeGeneratorException;
import com.qqc.apexsoft.codegenerator.utils.ExcelListener;
import com.qqc.apexsoft.codegenerator.utils.StringHelper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Component
public class BasicAutoCodeGenerator {
    private static final Logger log = LoggerFactory.getLogger(BasicAutoCodeGenerator.class);
    /**
     * 功能名 {functionName}Controller
     */
    @Value("${codeGenerator.field.function.name}")
    protected String functionName;
    protected String upperFunctionName;
    /**
     * 功能描述
     */
    private String functionDesc;
    /**
     * 方法名
     */
    protected String methodName;
    /**
     * 方法名 首字母大写
     */
    protected String upperMethodName;
    /**
     * 方法描述
     */
    protected String methodDesc;
    /**
     * 存储过程名/表名
     */
    private String procedureName;
    /**
     * 是否启用分页，默认为false，pageEnabled包含isReturnResult
     */
    protected boolean pageEnabled = false;
    /**
     * 是否返回结果集，默认为true
     */
    protected boolean isReturnResult;
    /**
     * 是否启用测试，默认为ture
     */
    private boolean testEnabled = true;
    /**
     * 是否创建文件，默认为false
     */
    private boolean isCreateFile = false;
    /**
     * proto根路径
     */
    private String protoSrcPath;
    /**
     * 客户端根路径
     */
    private String clientSrcPath;
    /**
     * 服务端根路径
     */
    private String serverSrcPath;
    /**
     * mapperXml根路径
     */
    private String mapperXmlSrcPath;
    /**
     * 服务端测试根路径
     */
    private String serverTestSrcPath;
    /**
     * 服务测试编译根路径
     */
    private String serverTestOutSrcPath;
    private String serviceName;
    private String protoPackageName;
    private String modelPackageName;
    private String controllerPackageName;
    private String consumerPackageName;
    private String providerPackageName;
    private String daoPackageName;
    private String daoImplPackageName;
    private String mapperPackageName;
    private String mapperXmlPackageName;
    private String testPackageName;
    private String testDataPackageName;
    private String protoName;
    private String modelName;
    private String controllerName;
    protected String consumerName;
    private String providerName;
    private String daoName;
    private String daoImplName;
    private String mapperName;
    private String mapperXmlName;
    private String testName;
    private String testDataName;
    private String modelDesc;
    /**
     * 默认包名前缀
     */
    private String defaultPackageNamePrefix;
    /**
     * proto包路径，新增徐修改
     */
    private String protoPackageUrl;
    /**
     * list索引，起始为2
     */
    private int index = 2;

    /**
     * 字段索引，默认为1
     */
    protected int fieldIndex = 1;

    /**
     * 接口索引，默认为1
     */
    private int interfaceIndex = 1;

    /**
     * 基准类
     */
    protected static Class<?> basicClass;

    /**
     * 匹配proto的Service的正则
     */
    private String rpcRegExp = ".*Service\\s+.*";

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

    /**
     * 白名单
     */
    protected String whiteRegExp;

    /**
     * 是否Get请求，默认false
     */
    private boolean isGet = false;

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

    private Class<?> getBasicClass() {
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

    protected List<ImportDataModel> getDataList(int sheetNo) {
        List<ImportDataModel> dataList = new ArrayList<>();
        for (Map<Integer, String> map : readExcel(sheetNo, getBasicClass())) {
            String name = map.get(3);//第四列
            String desc = map.get(0);//第一列
            String listObjName = upper(map.get(4));
            ImportDataModel importDataModel = new ImportDataModel();
            importDataModel.setName(name);
            importDataModel.setDesc(desc);
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

    protected String getPath(String name) {
        Assert.notNull(protoSrcPath);
        Assert.notNull(clientSrcPath);
        Assert.notNull(serverSrcPath);
        Assert.notNull(mapperXmlSrcPath);
        Assert.notNull(serverTestSrcPath);
        Assert.notNull(serverTestOutSrcPath);
        Assert.notNull(defaultPackageNamePrefix);
        Assert.notNull(functionName);
        String var0 = null;//根路径
        String var1 = null;//包路径
        String var2 = null;//文件名
        String var3 = ".java";//文件后缀
        switch (name) {
            case "proto":
                var0 = protoSrcPath;
                var1 = stringHelper.replace(protoPackageName).getPath();
                var2 = protoName;
                var3 = ".proto";
                break;
            case "model":
                var0 = clientSrcPath;
                var1 = stringHelper.replace(getPackageName(name)).getPath();
                var2 = StringUtils.isBlank(modelName) ? upper(methodName) + "Model" : modelName;
                break;
            case "controller":
                var0 = clientSrcPath;
                var1 = stringHelper.replace(getPackageName("controller")).getPath();
                var2 = StringUtils.isBlank(controllerName) ? functionName + "Controller" : controllerName;
                break;
            case "consumer":
                var0 = clientSrcPath;
                var1 = stringHelper.replace(getPackageName("consumer")).getPath();
                var2 = StringUtils.isBlank(consumerName) ? functionName + "Consumer" : consumerName;
                break;
            case "provider":
                var0 = serverSrcPath;
                var1 = stringHelper.replace(getPackageName("provider")).getPath();
                var2 = StringUtils.isBlank(providerName) ? functionName + "Provider" : providerName;
                break;
            case "dao":
                var0 = serverSrcPath;
                var1 = stringHelper.replace(getPackageName("dao")).getPath();
                var2 = StringUtils.isBlank(daoName) ? functionName + "Dao" : daoName;
                break;
            case "daoImpl":
                var0 = serverSrcPath;
                var1 = stringHelper.replace(getPackageName("daoImpl")).getPath();
                var2 = StringUtils.isBlank(daoImplName) ? functionName + "DaoImpl" : daoImplName;
                break;
            case "mapper":
                var0 = serverSrcPath;
                var1 = stringHelper.replace(getPackageName("mapper")).getPath();
                var2 = StringUtils.isBlank(mapperName) ? functionName + "Mapper" : mapperName;
                break;
            case "mapperXml":
                var0 = mapperXmlSrcPath;
                var1 = "";
                var2 = StringUtils.isBlank(mapperXmlName) ? functionName + "Mapper" : mapperXmlName;
                var3 = ".xml";
                break;
            case "test":
                var0 = serverTestSrcPath;
                var1 = stringHelper.replace(getPackageName("test")).getPath();
                var2 = StringUtils.isBlank(testName) ? functionName + "Test" : testName;
                break;
            case "testData":
                var0 = serverTestOutSrcPath;
                var1 = stringHelper.replace(getPackageName("testData")).getPath();
                var2 = interfaceIndex + "_" + methodName;
                var3 = "";
                break;
        }
        return stringHelper.concat(var0).concatPath(var1).concatPath(var2).concat(var3).getPath();
    }

    protected String getPackageName(String mainName) {
        Objects.requireNonNull(mainName);
        String var = null;
        switch (mainName) {
            case "controller":
                if (!StringUtils.isBlank(controllerPackageName)) {
                    return controllerPackageName;
                } else {
                    var = mainName;
                }
                break;
            case "consumer":
                if (!StringUtils.isBlank(controllerPackageName)) {
                    return controllerPackageName;
                } else {
                    var = "service";
                }
                break;
            case "proto":
                return protoPackageName;
            case "provider":
                if (!StringUtils.isBlank(providerName)) {
                    return providerName;
                } else {
                    var = mainName;
                }
                break;
            case "dao":
                if (!StringUtils.isBlank(daoPackageName)) {
                    return daoPackageName;
                } else {
                    var = mainName;
                }
                break;
            case "daoImpl":
                if (!StringUtils.isBlank(daoImplPackageName)) {
                    return daoImplPackageName;
                } else {
                    var = "dao.impl";
                }
                break;
            case "mapper":
                if (!StringUtils.isBlank(mapperPackageName)) {
                    return mapperPackageName;
                } else {
                    var = mainName;
                }
                break;
            case "test":
            case "testData":
                return StringUtils.isBlank(testDataPackageName) ? defaultPackageNamePrefix : testDataPackageName;
            default:
                var = mainName;
                break;
        }
        return defaultPackageNamePrefix.concat(".").concat(var);
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
                bw.write("\n");
            }

            // 开始写新代码
            StringBuilder sb = new StringBuilder();
            sb.append(context);
            sb.append(line == null ? "" : line);
            bw.write(sb.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        log.info("write成功,path:{},context:{}", path, context);
    }

    /**
     * 写入除proto追加内容之外的所有内容
     * 包括新增内容和追加内容
     * @param path 路径
     * @param context 内容
     */
    protected void write0(String path, String context) {
        setPath(path);
        log.info("setPath:" + path);
        setOriginData();
        write(context);
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
                    if (pre.matches(rpcRegExp)) {
                        bw.write(context1);
                        bw.write("\n");
                        matchedRPC = true;
                    }
                }

                if (matchedRPC) {
                    log.info("使用正则[" + rpcRegExp + "]成功匹配到proto Service");
                } else {
                    String msg = "没有匹配到proto Service！！！";
                    throw new AutoCodeGeneratorException(msg);
                }
                // 开始写新代码
                StringBuilder sb = new StringBuilder();
                if (interfaceIndex != 1) {
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
//        log.info("write1,context1:{},context2:{}", context1, context2);
    }

    /**
     * 写入proto追加内容
     * @param path proto路径
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
        if (!isCreateFile) {
            assertAndLog("proto");
            assertAndLog("controller");
            assertAndLog("consumer");
            assertAndLog("provider");
            assertAndLog("dao");
            assertAndLog("daoImpl");
            assertAndLog("mapper");
            assertAndLog("mapperXml");
            assertAndLog("test");
        }
        log.info("文件路径检查通过");
    }

    private void assertAndLog(String name) {
        String path = getPath(name);
        log.info(name + ":" + path);
        Assert.isTrue(new File(path).exists());
    }

    public boolean getIsCreateFile() {
        return isCreateFile;
    }

    public void setIsCreateFile(boolean isCreateFile) {
        this.isCreateFile = isCreateFile;
    }

    public void setIsReturnResult(boolean isReturnResult) {
        this.isReturnResult = isReturnResult;
    }

    public boolean getIsReturnResult() {
        return isReturnResult;
    }

    public boolean getPageEnabled() {
        return pageEnabled;
    }

    public boolean isGet() {
        return isGet;
    }

    public void setGet(boolean get) {
        isGet = get;
    }

    protected String getConsumerName() {
        if (StringUtils.isBlank(controllerName)) {
            return functionName + "Consumer";
        }
        return consumerName;
    }

    public void setUpperMethodName() {
        this.upperMethodName = upper(methodName);
    }

    public void setUpperFunctionName() {
        this.upperFunctionName = upper(functionName);
    }

    protected String getServiceName() {
        if (StringUtils.isBlank(serviceName)) {
            return upperFunctionName + "Service";
        }
        return serviceName;
    }

    protected String getModelName() {
        if (StringUtils.isBlank(modelName)) {
            return upperMethodName + "Model";
        }
        return modelName;
    }

    public String getModelDesc() {
        if (StringUtils.isBlank(modelDesc)) {
            return methodDesc;
        }
        return modelDesc;
    }
}
