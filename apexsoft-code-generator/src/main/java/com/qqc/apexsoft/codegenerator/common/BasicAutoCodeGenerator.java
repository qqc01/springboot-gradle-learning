package com.qqc.apexsoft.codegenerator.common;

import com.alibaba.excel.EasyExcel;
import com.qqc.apexsoft.codegenerator.config.CodeGeneratorConfiguration;
import com.qqc.apexsoft.codegenerator.model.ImportDataModel;
import com.qqc.apexsoft.codegenerator.model.ImportDataType;
import com.qqc.apexsoft.codegenerator.utils.AutoCodeGeneratorException;
import com.qqc.apexsoft.codegenerator.utils.ExcelListener;
import com.qqc.apexsoft.codegenerator.utils.StringHelper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.*;
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
                break;
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
                break;
            case "mapperXml":
                var0 = configuration.mapperXmlSrcPath;
                var2 = configuration.upperFunctionName + "Mapper";
                var3 = ".xml";
                break;
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
}
