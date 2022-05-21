package com.qqc.apexsoft.codegenerator.queryList;

import com.qqc.apexsoft.codegenerator.config.AutoCodeGeneratorConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = AutoCodeGeneratorConfig.class)
public class QueryListAutoCodeNewGeneratorTest implements ApplicationContextAware, InitializingBean {
    public static final Logger log = LoggerFactory.getLogger(QueryListAutoCodeNewGeneratorTest.class);
    private QueryListAutoCodeNewGenerator queryListAutoCodeNewGenerator;
    private ApplicationContext applicationContext;

    @Test
    public void contextLoads() {
        System.out.println(queryListAutoCodeNewGenerator);
    }

    @Test
    public void writeProto() {
        queryListAutoCodeNewGenerator.writeProto();
    }

    @Test
    public void writeModel() {
        queryListAutoCodeNewGenerator.writeModel();
    }

    @Test
    public void writeController() {
        queryListAutoCodeNewGenerator.writeController();
    }

    @Test
    public void test() {
        System.out.println(queryListAutoCodeNewGenerator.getFunctionName());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        QueryListAutoCodeNewGenerator queryListAutoCodeNewGenerator = (QueryListAutoCodeNewGenerator) applicationContext.getBean("queryListAutoCodeNewGenerator");
        // 业务配置
//        queryListAutoCodeNewGenerator.setFunctionName("otherService");//功能名
        queryListAutoCodeNewGenerator.setFunctionDesc("机构C5-服务模块基础微服务");
        queryListAutoCodeNewGenerator.setMethodName("queryCusMaintainList1");//方法名，修改
        queryListAutoCodeNewGenerator.setMethodDesc("客户维护查询");//方法描述，修改
        queryListAutoCodeNewGenerator.setProcedureName("PCX_JC_KHWHCX");//存储过程名/表名，修改
        queryListAutoCodeNewGenerator.setPageEnabled(true);//是否启用分页，默认为false，pageEnabled包含isReturnResult
        queryListAutoCodeNewGenerator.setIsReturnResult(true);//是否返回结果集，默认为true
        queryListAutoCodeNewGenerator.setTestEnabled(true);//是否启用测试，默认为ture
        queryListAutoCodeNewGenerator.setIsCreateFile(true);//是否创建文件，默认为false
        queryListAutoCodeNewGenerator.setInterfaceIndex(1);//接口索引，默认为1
        // 根路径
        queryListAutoCodeNewGenerator.setProtoSrcPath("D:\\IdeaProjects\\apexsoft\\swhy\\work01\\jgcrm_ams\\ams-protocol\\src\\main\\proto");
        queryListAutoCodeNewGenerator.setClientSrcPath("D:\\IdeaProjects\\apexsoft\\swhy\\work01\\jgcrm_ams\\ams-client-org-activity\\src\\main\\java");
        queryListAutoCodeNewGenerator.setServerSrcPath("D:\\IdeaProjects\\apexsoft\\swhy\\work01\\jgcrm_ams\\ams-server-org-activity\\src\\main\\java");
        queryListAutoCodeNewGenerator.setMapperXmlSrcPath("D:\\IdeaProjects\\apexsoft\\swhy\\work01\\jgcrm_ams\\ams-server-org-activity\\src\\main\\resources\\mybatis\\oracle\\jgcrm");
        queryListAutoCodeNewGenerator.setServerTestSrcPath("D:\\IdeaProjects\\apexsoft\\swhy\\work01\\jgcrm_ams\\ams-server-org-activity\\src\\test\\java");
        queryListAutoCodeNewGenerator.setServerTestOutSrcPath("D:\\IdeaProjects\\apexsoft\\swhy\\work01\\jgcrm_ams\\ams-server-org-activity\\out\\test\\classes");
        // 默认设置
        queryListAutoCodeNewGenerator.setDefaultPackageNamePrefix("com.apexsoft.crm.otheractivity");
        queryListAutoCodeNewGenerator.setWhiteRegExp("czr");
        // 其他设置
        // proto
        queryListAutoCodeNewGenerator.setProtoPackageName("ip.basic.activity");
        queryListAutoCodeNewGenerator.setProtoName("activity1");
        queryListAutoCodeNewGenerator.setRpcRegExp(".*Service\\s+.*");
        // 首字母大写
        queryListAutoCodeNewGenerator.setUpperFunctionName();
        queryListAutoCodeNewGenerator.setUpperMethodName();
        queryListAutoCodeNewGenerator.fileCheck();
        this.queryListAutoCodeNewGenerator = queryListAutoCodeNewGenerator;
    }
}
