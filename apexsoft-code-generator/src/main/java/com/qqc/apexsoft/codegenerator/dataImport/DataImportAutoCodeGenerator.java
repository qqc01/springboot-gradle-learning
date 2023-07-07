package com.qqc.apexsoft.codegenerator.dataImport;

import com.qqc.apexsoft.codegenerator.common.AutoCodeGenerator;
import com.qqc.apexsoft.codegenerator.common.BasicAutoCodeGenerator;
import com.qqc.apexsoft.codegenerator.model.ImportDataModel;
import com.qqc.apexsoft.codegenerator.model.ImportDataType;
import com.qqc.apexsoft.codegenerator.utils.AutoCodeGeneratorHelper;
import com.qqc.apexsoft.codegenerator.utils.JSONUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 适用场景
 * 1、新增内容和追加内容
 * 2、测试模块
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
public class DataImportAutoCodeGenerator extends BasicAutoCodeGenerator implements AutoCodeGenerator, InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(DataImportAutoCodeGenerator.class);
    private AutoCodeGeneratorHelper helper;

    static {
        setBasicClass(DataImportAutoCodeGenerator.class);
    }

    @Override
    public void autoCodeGenerate() {
        AutoCodeGenerator.super.autoCodeGenerate();
        writeBean();
    }

    /**
     * 编写proto
     * <p>
     * 1、新增/追加
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
     * 1、新增/追加
     */
    @Override
    public void writeModel() {
        mainName = "model";
        writeModel(getPath(mainName), inList);
    }

    /**
     * 编写导入bean
     * <p>
     * 1、新增/追加
     */
    public void writeBean() {
        modelName = configuration.upperMethodName.concat("Bean");
        mainName = "model";
        write0(getPath(mainName), getBeanNewString());
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
     * 1、新增/追加
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
    public void writeClientTest() {}

    @Override
    public void writeServerTest() {}

    @Override
    public void writeTestData() {}

    @Override
    public void afterPropertiesSet() {
        fileCheck();
    }

    List<ImportDataModel> excelTitleList;

    public void config() {
        // 入参
        List<ImportDataModel> inList = getDataList(0);
        setInList(inList);
        log.info("setInList:{}", JSONUtil.getJSONStandardString(inList));
        // 出参
        List<ImportDataModel> outList = getDataList(1);
        setOutList(outList);
        log.info("setOutList:{}", JSONUtil.getJSONStandardString(outList));
        // excel title
        excelTitleList = getDataList(2);
        log.info("excelTitleList:{}", JSONUtil.getJSONStandardString(excelTitleList));
    }

    public String getProtoAppendString() {
        int replaceCount = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(replaceFormat("/************************************** {}开始 **************************************/\n", 1));
        // 1 req
        sb.append(replaceFormat("message {}Req {\n", replaceCount++));
        // 1.2 默认必须有的入参
        sb.append("\t//导入人\n").append(replaceAll("\tint32 importer = {0};\n", fieldIndex++));
        sb.append(replaceFormat(replaceAll("\trepeated {} {} = {0};\n", fieldIndex++), 0, replaceCount++));
        sb.append("}\n");
        // 空行
        sb.append("\n");

        // 导入数据
        sb.append(replaceFormat("message {} {\n", 0));
        sb.append(getProtoBody(excelTitleList));
        sb.append("}\n");
        // 空行
        sb.append("\n");

        // rsp
        sb.append(replaceFormat("message {}Rsp {\n", 0));
        sb.append(replaceAll("\tint32 code = {0}; //返回值\n", fieldIndex++));
        sb.append(replaceAll("\tstring note = {0}; //返回消息\n", fieldIndex++));
        sb.append(replaceAll("\tint32 total = {0}; //总记录数\n", fieldIndex++));
        sb.append("}\n");
        sb.append(replaceFormat("/************************************** {}结束 **************************************/\n", 1));
        String protoAppendString = replaceAll(sb,
                configuration.upperMethodName,
                configuration.methodName);
        log.info("构建protoAppendString:\n{}", protoAppendString);
        return protoAppendString;
    }

    private String getBeanNewString() {
        int replaceCount = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(replaceFormat("package {};\n", replaceCount++));
        sb.append("\n");
        sb.append("import com.alibaba.excel.annotation.ExcelProperty;\n");
        sb.append("\n");
        sb.append(replaceFormat("public class {}Bean {\n", replaceCount++));
        // 实例变量
        sb.append(getBeanBody(excelTitleList));
        // getter and setter
        // 默认操作人
        sb.append("\n");
        sb.append(getModelGetterAndSetter(excelTitleList));

        // 去除最后一个空行
        deleteEnd(sb);

        sb.append("}\n");
        String newBeanString = replaceAll(sb, getPackageName(mainName), configuration.upperMethodName);
        log.info("构建newBeanString:\n{}", newBeanString);
        return newBeanString;
    }

    private String getBeanBody(List<ImportDataModel> excelTitleList) {
        StringBuilder sb = new StringBuilder();
        for (ImportDataModel importDataModel : excelTitleList) {
            StringBuilder temp = new StringBuilder();
            temp.append("\t@ExcelProperty(\"{0}\")\n").append("\tprivate String {1};\n");
            sb.append(replaceAll(temp, importDataModel.getDesc(), importDataModel.getName()));
        }
        return sb.toString();
    }

    private String getConsumerAppendString() {
        int replaceCount = 0;
        StringBuilder sb = new StringBuilder();
        // 添加首行空行
//        sb.append("\n");
        sb.append(replaceFormat("\tpublic {}Rsp {}({}Model model) {\n", replaceCount++, replaceCount++, 0));
        sb.append(replaceFormat("\t\t{}Req.Builder request = {}Req.newBuilder();\n", 0, 0));
        sb.append("\t\tString md5 = model.getMd5();\n");
        sb.append("\t\tif (!StringUtils.isBlank(md5)) {\n");
        sb.append("\t\t\tMap map = fileStorageService.downloadRedisFile(md5);\n");
        sb.append("\t\t\tInputStream inp = new ByteArrayInputStream((byte[]) map.get(\"fileByte\"));\n");
        sb.append(replaceFormat("\t\t\tExcelListener<{}Bean> listener = new ExcelListener<>();\n", 0));
        sb.append(replaceFormat("\t\t\tEasyExcel.read(inp, {}Bean.class, listener).sheet().doRead();\n", 0));
        sb.append(replaceFormat("\t\t\tList<{}Bean> data = listener.getData();\n", 0));
        sb.append(replaceFormat("\t\t\tdata.forEach({}Bean -> {\n", 1));
        sb.append(replaceFormat("\t\t\t\t{}.Builder {}Builder = {}.newBuilder();\n", 0, 1, 0));
        sb.append(replaceFormat("\t\t\t\tProtoBufUtil.transform({}Bean, {}Builder);\n", 1, 1));
        sb.append(replaceFormat("\t\t\t\trequest.add{}({}Builder);\n", 0, 1));
        sb.append("\t\t\t});\n");
        sb.append("\t\t\trequest.setImporter(UserHelper.getId());\n");
        sb.append("\t\t}\n");
        sb.append(replaceFormat("\t\treturn {}ServiceBlockingStub.{}(request.build());\n", replaceCount++, 1));
        sb.append("\t}\n");
        String consumerAppendString = replaceAll(sb,
                configuration.upperMethodName,
                configuration.methodName,
                configuration.functionName);
        log.info("consumerAppendString:\n{}", consumerAppendString);
        return consumerAppendString;
    }

    protected String getProviderAppendString() {
        int replaceCount = 0;
        StringBuilder sb = new StringBuilder();
        String providerAppendString = null;
        if (!configuration.isDirectImportTable) {
            // 添加首行空行
//        sb.append("\n");
            sb.append("\t@Override\n");
            sb.append(replaceFormat("\tpublic void {}({}Req request, StreamObserver<{}Rsp> responseObserver) {\n", replaceCount++, replaceCount++, 1));
            sb.append(replaceFormat("\t\t{}Rsp.Builder rsp = {}Rsp.newBuilder();\n", 1, 1));
            sb.append(replaceFormat("\t\t{}Dao.deleteByDrrFor{}(request);\n", replaceCount++, 1));
            sb.append(replaceFormat("\t\tInteger count = {}Dao.{}(request.get{}List(), request);\n", 2, 0, 1));
//        sb.append("\t\tMap<String, Object> ins = new HashMap<>();\n");
//        sb.append("\t\tins.put(\"I_CZR\", request.getImporter());\n");
//        sb.append(replaceFormat("\t\t{}Dao.handle{}(ins);\n", 2, 1));
//        sb.append("\t\tObject code = ins.get(\"O_CODE\");\n");
//        sb.append("\t\tint o_code = 0;\n");
//        sb.append("\t\tif (code != null) {\n");
//        sb.append("\t\t\to_code = Integer.parseInt(String.valueOf(ins.get(\"O_CODE\")));\n");
//        sb.append("\t\t}\n");
//        sb.append("\t\tif (o_code > 0) {\n");
//        sb.append("\t\t\trsp.setCode(o_code).setNote(\"成功导入\" + count + \"条数据到数据库\").setTotal(count == null ? 0 : count);\n");
//        sb.append("\t\t} else {\n");
//        sb.append("\t\t\trsp.setCode(-1).setNote(ParamUtils.getString(ins.get(\"O_NOTE\"))).setTotal(0);\n");
//        sb.append("\t\t}\n");
            sb.append("\t\trsp.setCode(SUCCESS_CODE).setNote(\"成功导入\" + count + \"条数据到数据库\").setTotal(count == null ? 0 : count);\n");
            sb.append("\t\tresponseObserver.onNext(rsp.build());\n");
            sb.append("\t\tresponseObserver.onCompleted();\n");
            sb.append("\t}\n");
            providerAppendString = replaceAll(sb,
                    configuration.methodName,
                    configuration.upperMethodName,
                    configuration.functionName);
        } else {
            // 添加首行空行
//        sb.append("\n");
            sb.append("\t@Override\n");
            sb.append(replaceFormat("\tpublic void {}({}Req request, StreamObserver<{}Rsp> responseObserver) {\n", replaceCount++, replaceCount++, 1));
            sb.append(replaceFormat("\t\t{}Rsp.Builder rsp = {}Rsp.newBuilder();\n", 1, 1));
            sb.append(replaceFormat("\t\tInteger count = {}Dao.{}(request.get{}List(), request);\n", 2, 0, 1));
//        sb.append("\t\tMap<String, Object> ins = new HashMap<>();\n");
//        sb.append("\t\tins.put(\"I_CZR\", request.getImporter());\n");
//        sb.append(replaceFormat("\t\t{}Dao.handle{}(ins);\n", 2, 1));
//        sb.append("\t\tObject code = ins.get(\"O_CODE\");\n");
//        sb.append("\t\tint o_code = 0;\n");
//        sb.append("\t\tif (code != null) {\n");
//        sb.append("\t\t\to_code = Integer.parseInt(String.valueOf(ins.get(\"O_CODE\")));\n");
//        sb.append("\t\t}\n");
//        sb.append("\t\tif (o_code > 0) {\n");
//        sb.append("\t\t\trsp.setCode(o_code).setNote(\"成功导入\" + count + \"条数据到数据库\").setTotal(count == null ? 0 : count);\n");
//        sb.append("\t\t} else {\n");
//        sb.append("\t\t\trsp.setCode(-1).setNote(ParamUtils.getString(ins.get(\"O_NOTE\"))).setTotal(0);\n");
//        sb.append("\t\t}\n");
            sb.append("\t\trsp.setCode(1).setNote(\"成功导入\" + count + \"条数据到数据库\").setTotal(count == null ? 0 : count);\n");
            sb.append("\t\tresponseObserver.onNext(rsp.build());\n");
            sb.append("\t\tresponseObserver.onCompleted();\n");
            sb.append("\t}\n");
            providerAppendString = replaceAll(sb,
                    configuration.methodName,
                    configuration.upperMethodName,
                    configuration.functionName);
        }

        log.info("providerAppendString:\n{}", providerAppendString);
        return providerAppendString;
    }

    protected String getDaoAppendString() {
        int replaceCount = 0;
        StringBuilder sb = new StringBuilder();
//        if (configuration.interfaceIndex != 1) {
//            // 添加首行空行
//            sb.append("\n");
//        }
        String daoAppendString = null;
        if (!configuration.isDirectImportTable) {
            sb.append(replaceFormat("\tvoid deleteByDrrFor{}({}Req req);\n", replaceCount++, 0));
            sb.append("\n");
            sb.append(replaceFormat("\tInteger {}(List<{}> dataList, {}Req req);\n", replaceCount++, 0, 0));
//        sb.append("\n");
//        sb.append(replaceFormat("\tvoid handle{}(Map<String, Object> ins);\n", 0));
            daoAppendString = replaceAll(sb, configuration.upperMethodName, configuration.methodName);
        } else {
            sb.append(replaceFormat("\tInteger {}(List<{}> dataList, {}Req req);\n", 1, 0, 0));
//        sb.append("\n");
//        sb.append(replaceFormat("\tvoid handle{}(Map<String, Object> ins);\n", 0));
            daoAppendString = replaceAll(sb, configuration.upperMethodName, configuration.methodName);
        }

        log.info("daoAppendString:\n{}", daoAppendString);
        return daoAppendString;
    }

    protected String getDaoImplAppendString() {
        int replaceCount = 0;
        String daoImplAppendString = null;
        StringBuilder sb = new StringBuilder();
        // 添加首行空行
//        sb.append("\n");
        if (!configuration.isDirectImportTable) {
            sb.append("\t@Override\n");
            sb.append(replaceFormat("\tpublic void deleteByDrrFor{}({}Req req) {\n", replaceCount++, 0));
            sb.append(replaceFormat("\t\t{}Mapper.deleteByDrrFor{}(req);\n", replaceCount++, 0));
            sb.append("\t}\n");
            sb.append("\n");
            sb.append("\t@Override\n");
            sb.append(replaceFormat("\tpublic Integer {}(List<{}> dataList, {}Req req) {\n", replaceCount++, 0, 0));
            sb.append("\t\tint count = 0;\n");
            sb.append("\t\tint mod = dataList.size() / 1000;\n");
            sb.append(replaceFormat("\t\tList<{}> subList = null;\n", 0));
            sb.append("\t\tfor (int i = 0; i <= mod; i++) {\n");
            sb.append("\t\t\tsubList = dataList.subList(1000 * i, Math.min(1000 * (i + 1), dataList.size()));\n");
            sb.append("\t\t\tif (!CollectionUtils.isEmpty(subList)) {\n");
            sb.append(replaceFormat("\t\t\t\tcount += {}Mapper.{}(subList, req);\n", 1, 2));
            sb.append("\t\t\t}\n");
            sb.append("\t\t}\n");
            sb.append("\t\treturn count;\n");
            sb.append("\t}\n");
//        sb.append("\n");
//        sb.append("\t@Override\n");
//        sb.append(replaceFormat("\tpublic void handle{}(Map<String, Object> ins) {\n", 0));
//        sb.append(replaceFormat("\t\t{}Mapper.handle{}(ins);\n", 1, 0));
//        sb.append("\t}\n");
            daoImplAppendString = replaceAll(sb,
                    configuration.upperMethodName,
                    configuration.functionName,
                    configuration.methodName);
        } else {
            sb.append("\t@Override\n");
            sb.append(replaceFormat("\tpublic Integer {}(List<{}> dataList, {}Req req) {\n", 2, 0 , 0));
            sb.append("\t\tint count = 0;\n");
            sb.append("\t\tint mod = dataList.size() / 1000;\n");
            sb.append(replaceFormat("\t\tList<{}> subList = null;\n", 0));
            sb.append("\t\tfor (int i = 0; i <= mod; i++) {\n");
            sb.append("\t\t\tsubList = dataList.subList(1000 * i, Math.min(1000 * (i + 1), dataList.size()));\n");
            sb.append("\t\t\tif (!CollectionUtils.isEmpty(subList)) {\n");
            sb.append(replaceFormat("\t\t\t\tcount += {}Mapper.{}(subList, req);\n", 1, 2));
            sb.append("\t\t\t}\n");
            sb.append("\t\t}\n");
            sb.append("\t\treturn count;\n");
            sb.append("\t}\n");
//        sb.append("\n");
//        sb.append("\t@Override\n");
//        sb.append(replaceFormat("\tpublic void handle{}(Map<String, Object> ins) {\n", 0));
//        sb.append(replaceFormat("\t\t{}Mapper.handle{}(ins);\n", 1, 0));
//        sb.append("\t}\n");
            daoImplAppendString = replaceAll(sb,
                    configuration.upperMethodName,
                    configuration.functionName,
                    configuration.methodName);
        }

        log.info("daoImplAppendString:\n{}", daoImplAppendString);
        return daoImplAppendString;
    }

    protected String getMapperAppendString() {
        int replaceCount = 0;
        String mapperAppendString = null;
        StringBuilder sb = new StringBuilder();
//        if (configuration.interfaceIndex != 1) {
//            // 添加首行空行
//            sb.append("\n");
//        }
        if (!configuration.isDirectImportTable) {
            sb.append(replaceFormat("\tvoid deleteByDrrFor{}({}Req req);\n", replaceCount++, 0));
            sb.append("\n");
            sb.append(replaceFormat("\tint {}(@Param(\"dataList\") List<{}> dataList, @Param(\"req\") {}Req req);\n", replaceCount++, 0, 0));
//        sb.append("\n");
//        sb.append(replaceFormat("\tvoid handle{}(Map<String, Object> ins);\n", 0));
            mapperAppendString = replaceAll(sb, configuration.upperMethodName, configuration.methodName);
        } else {
            sb.append(replaceFormat("\tint {}(@Param(\"dataList\") List<{}> dataList, @Param(\"req\") {}Req req);\n", 1, 0, 0));
//        sb.append("\n");
//        sb.append(replaceFormat("\tvoid handle{}(Map<String, Object> ins);\n", 0));
            mapperAppendString = replaceAll(sb, configuration.upperMethodName, configuration.methodName);
        }

        log.info("mapperAppendString:\n{}", mapperAppendString);
        return mapperAppendString;
    }

    protected String getMapperXmlAppendString() {
        int replaceCount = 0;
        String mapperXmlAppendString = null;
        StringBuilder sb = new StringBuilder();
//        if (configuration.interfaceIndex != 1) {
//            // 添加首行空行
//            sb.append("\n");
//        }
        if (!configuration.isDirectImportTable) {
            sb.append(replaceFormat("\t<delete id=\"deleteByDrrFor{}\">\n", replaceCount++));
            sb.append(replaceFormat("\t\tDELETE FROM {} WHERE DRR = #{importer} --导入人用户ID\n", replaceCount++));
            sb.append("\t</delete>\n");
            sb.append(replaceFormat("\t<insert id=\"{}\">\n", replaceCount++));
            sb.append("\t\tINSERT ALL\n");
            sb.append("\t\t<foreach collection =\"dataList\" item=\"data\">\n");
            sb.append(replaceFormat("\t\t\tINTO {} (\n", 1));
            excelTitleList.forEach(importDataModel -> sb.append(replaceAll("\t\t\t{0},\n", importDataModel.getProcedureParam())));
            sb.append("\t\t\tDRR)\n");
            sb.append("\t\t\tvalues (\n");
            excelTitleList.forEach(importDataModel -> sb.append(replaceAll("\t\t\t#{data.{0}},\n", importDataModel.getName())));
            sb.append("\t\t\t#{req.importer})\n");
            sb.append("\t\t</foreach>\n");
            sb.append("\t\tSELECT 1 FROM DUAL\n");
            sb.append("\t</insert>\n");
//        sb.append(replaceFormat("\t<parameterMap type=\"java.util.Map\" id=\"handle{}Map\">\n", 0));
//        sb.append("\t\t<parameter javaType=\"java.lang.Integer\" property=\"O_CODE\" mode=\"OUT\" jdbcType=\"INTEGER\"/>\n");
//        sb.append("\t\t<parameter javaType=\"java.lang.String\" property=\"O_NOTE\" mode=\"OUT\" jdbcType=\"VARCHAR\"/>\n");
//        sb.append("\t\t<parameter property=\"I_CZR\" mode=\"IN\"/>\n");
//        sb.append("\t</parameterMap>\n");
//        sb.append(replaceFormat("\t<update id=\"handle{}\" statementType=\"CALLABLE\" parameterMap=\"handle{}Map\">\n", 0, 0));
//        sb.append(replaceFormat("\t\tCALL {}(?,?,?)\n", replaceCount++));
//        sb.append("\t</update>\n");
            mapperXmlAppendString = replaceAll(sb,
                    configuration.upperMethodName,
                    configuration.procedureName,
                    configuration.methodName);
        } else {
            sb.append(replaceFormat("\t<insert id=\"{}\">\n", 2));
            sb.append("\t\tINSERT ALL\n");
            sb.append("\t\t<foreach collection =\"dataList\" item=\"data\">\n");
            sb.append(replaceFormat("\t\t\tINTO {} (\n", 1));
            sb.append("\t\t\tID,\n");
            excelTitleList.forEach(importDataModel -> sb.append(replaceAll("\t\t\t{0},\n", importDataModel.getProcedureParam())));
            // sb.append("\t\t\tDRR)\n");
            deleteEnd(sb);
            deleteEnd(sb);
            sb.append(")\n");
            sb.append("\t\t\tvalues (\n");
            // 检查是否为JGCRM.表名这种形式
            int index;
            String nextIdTableName = null;
            if ((index = configuration.procedureName.indexOf(".")) > 0) {
                nextIdTableName = configuration.procedureName.substring(index + 1);
            }
            sb.append(replaceAll("\t\t\tJGCRM.FUNC_NEXTID('{0}'),\n", nextIdTableName));
            excelTitleList.forEach(importDataModel -> sb.append(replaceAll("\t\t\t#{data.{0}},\n", importDataModel.getName())));
            deleteEnd(sb);
            deleteEnd(sb);
            sb.append(")\n");
            // sb.append("\t\t\t#{req.importer})\n");
            sb.append("\t\t</foreach >\n");
            sb.append("\t\tSELECT 1 FROM DUAL\n");
            sb.append("\t</insert>\n");
//        sb.append(replaceFormat("\t<parameterMap type=\"java.util.Map\" id=\"handle{}Map\">\n", 0));
//        sb.append("\t\t<parameter javaType=\"java.lang.Integer\" property=\"O_CODE\" mode=\"OUT\" jdbcType=\"INTEGER\"/>\n");
//        sb.append("\t\t<parameter javaType=\"java.lang.String\" property=\"O_NOTE\" mode=\"OUT\" jdbcType=\"VARCHAR\"/>\n");
//        sb.append("\t\t<parameter property=\"I_CZR\" mode=\"IN\"/>\n");
//        sb.append("\t</parameterMap>\n");
//        sb.append(replaceFormat("\t<update id=\"handle{}\" statementType=\"CALLABLE\" parameterMap=\"handle{}Map\">\n", 0, 0));
//        sb.append(replaceFormat("\t\tCALL {}(?,?,?)\n", replaceCount++));
//        sb.append("\t</update>\n");
            mapperXmlAppendString = replaceAll(sb,
                    configuration.upperMethodName,
                    configuration.procedureName,
                    configuration.methodName);
        }

        log.info("mapperXmlAppendString:\n{}", mapperXmlAppendString);
        return mapperXmlAppendString;
    }
}
