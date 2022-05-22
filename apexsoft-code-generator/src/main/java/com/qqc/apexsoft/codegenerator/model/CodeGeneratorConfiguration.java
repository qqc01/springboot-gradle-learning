package com.qqc.apexsoft.codegenerator.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class CodeGeneratorConfiguration {
    @Value("${codeGenerator.functionName}")
    public String functionName;
    public String upperFunctionName = upper(functionName);
    /**
     * 功能描述
     */
    @Value("${codeGenerator.functionDesc}")
    public String functionDesc;

    /**
     * 方法名
     */
    @Value("${codeGenerator.methodName}")
    public String methodName;
    /**
     * 方法名 首字母大写
     */
    public String upperMethodName = upper(methodName);
    /**
     * 方法描述
     */
    @Value("${codeGenerator.methodDesc}")
    public String methodDesc;
    /**
     * 存储过程名/表名
     */
    @Value("${codeGenerator.procedureName}")
    public String procedureName;
    /**
     * 是否启用分页，默认为false，pageEnabled包含isReturnResult
     */
    @Value("${codeGenerator.pageEnabled:false}")
    public boolean pageEnabled;
    /**
     * 是否返回结果集，默认为true
     */
    @Value("${codeGenerator.isReturnResult:true}")
    public boolean isReturnResult;
    /**
     * 是否启用测试，默认为ture
     */
    @Value("${codeGenerator.testEnabled:true}")
    public boolean testEnabled = true;
    /**
     * 是否创建文件，默认为false
     */
    @Value("${codeGenerator.isCreateFile:false}")
    public boolean isCreateFile = false;
    /**
     * proto根路径
     */
    @Value("${codeGenerator.protoSrcPath}")
    public String protoSrcPath;
    /**
     * 客户端根路径
     */
    @Value("${codeGenerator.clientSrcPath}")
    public String clientSrcPath;
    /**
     * 服务端根路径
     */
    @Value("${codeGenerator.serverSrcPath}")
    public String serverSrcPath;
    /**
     * mapperXml根路径
     */
    @Value("${codeGenerator.mapperXmlSrcPath}")
    public String mapperXmlSrcPath;
    /**
     * 服务端测试根路径
     */
    @Value("${codeGenerator.serverTestSrcPath}")
    public String serverTestSrcPath;
    /**
     * 服务测试编译根路径
     */
    @Value("${codeGenerator.serverTestOutSrcPath}")
    public String serverTestOutSrcPath;
    @Value("${codeGenerator.serviceName}")
    public String serviceName;
    @Value("${codeGenerator.serverTestOutSrcPath}")
    public String protoPackageName;
    @Value("${codeGenerator.modelPackageName}")
    public String modelPackageName;
    @Value("${codeGenerator.controllerPackageName}")
    public String controllerPackageName;
    @Value("${codeGenerator.consumerPackageName}")
    public String consumerPackageName;
    @Value("${codeGenerator.providerPackageName}")
    public String providerPackageName;
    @Value("${codeGenerator.daoPackageName}")
    public String daoPackageName;
    @Value("${codeGenerator.daoImplPackageName}")
    public String daoImplPackageName;
    @Value("${codeGenerator.mapperPackageName}")
    public String mapperPackageName;
    @Value("${codeGenerator.mapperXmlPackageName}")
    public String mapperXmlPackageName;
    @Value("${codeGenerator.testPackageName}")
    public String testPackageName;
    @Value("${codeGenerator.testDataPackageName}")
    public String testDataPackageName;
    @Value("${codeGenerator.protoName}")
    public String protoName;
    @Value("${codeGenerator.modelName}")
    public String modelName;
    @Value("${codeGenerator.controllerName}")
    public String controllerName;
    @Value("${codeGenerator.consumerName}")
    public String consumerName;
    @Value("${codeGenerator.providerName}")
    public String providerName;
    @Value("${codeGenerator.daoName}")
    public String daoName;
    @Value("${codeGenerator.daoImplName}")
    public String daoImplName;
    @Value("${codeGenerator.mapperName}")
    public String mapperName;
    @Value("${codeGenerator.mapperXmlName}")
    public String mapperXmlName;
    @Value("${codeGenerator.testName}")
    public String testName;
    @Value("${codeGenerator.testDataName}")
    public String testDataName;
    @Value("${codeGenerator.modelDesc}")
    public String modelDesc;
    /**
     * 默认包名前缀
     */
    @Value("${codeGenerator.defaultPackageNamePrefix}")
    public String defaultPackageNamePrefix;
    /**
     * proto包路径，新增徐修改
     */
    @Value("${codeGenerator.protoPackageUrl}")
    public String protoPackageUrl;
    /**
     * 白名单
     */
    @Value("${codeGenerator.whiteRegExp:czr}")
    public String whiteRegExp;
    /**
     * 匹配proto的Service的正则
     */
    @Value("${codeGenerator.rpcRegExp:.*Service\\s+.*}")
    public String rpcRegExp;

    /**
     * 接口索引，默认为1
     */
    @Value("${codeGenerator.interfaceIndex:1}")
    public int interfaceIndex;

    public String upper(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return String.valueOf(str.charAt(0)).toUpperCase().concat(str.substring(1));
    }
}
