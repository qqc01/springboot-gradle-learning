package com.qqc.apexsoft.codegenerator.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.qqc.apexsoft.codegenerator.utils.StringUtil.upper;

@Component
@Data
public class CodeGeneratorConfiguration implements InitializingBean {
    @Value("${codeGenerator.functionName}")
    public String functionName;

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
     * 表名
     */
    @Value("${codeGenerator.tableName}")
    public String tableName;

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
    @Value("${codeGenerator.serverTestDataSrcPath}")
    public String serverTestDataSrcPath;

    @Value("${codeGenerator.serviceName}")
    public String serviceName;

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
    private String whiteRegExp;

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

    @Value("${codeGenerator.protoName}")
    public String protoName;

    @Value("${codeGenerator.protoPackageName}")
    public String protoPackageName;

    public String upperFunctionName;

    public String upperMethodName;

    public String upperServiceName;

    @Value("${codeGenerator.controllerName}")
    public String controllerName;

    @Value("${codeGenerator.consumerName}")
    public String consumerName;

    @Value("${codeGenerator.consumerVariableName}")
    public String consumerVariableName;

    @Value("${codeGenerator.requestModelVariableName}")
    public String requestModelVariableName;

    @Value("${codeGenerator.blockingStubVariableName}")
    public String blockingStubVariableName;

    @Value("${codeGenerator.providerName}")
    public String providerName;

    @Value("${codeGenerator.daoVariableName}")
    public String daoVariableName;

    @Value("${codeGenerator.mapperVariableName}")
    public String mapperVariableName;

    @Value("${codeGenerator.mapperPath}")
    public String mapperPath;

    @Value("${codeGenerator.mapperXmlPath}")
    public String mapperXmlPath;

    @Value("${codeGenerator.protoPath}")
    public String protoPath;

    @Value("${codeGenerator.enabledNewVersion}")
    public Boolean enabledNewVersion;

    @Value("${codeGenerator.isCallProcedure}")
    public Boolean isCallProcedure;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.upperFunctionName = upper(functionName);
        this.upperMethodName = upper(methodName);
        this.upperServiceName = upper(serviceName);
    }
}
