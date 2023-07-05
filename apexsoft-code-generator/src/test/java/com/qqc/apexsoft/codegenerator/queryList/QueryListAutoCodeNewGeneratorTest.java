package com.qqc.apexsoft.codegenerator.queryList;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qqc.apexsoft.codegenerator.config.AutoCodeGeneratorConfig;
import com.qqc.apexsoft.codegenerator.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;

import static com.qqc.apexsoft.codegenerator.utils.StringUtil.upper;

@SpringBootTest
@ContextConfiguration(classes = AutoCodeGeneratorConfig.class)
public class QueryListAutoCodeNewGeneratorTest {
    public static final Logger log = LoggerFactory.getLogger(QueryListAutoCodeNewGeneratorTest.class);
    @Autowired(required = false)
    private QueryListAutoCodeNewGenerator queryListAutoCodeNewGenerator;

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
    public void writeConsumer() {
        queryListAutoCodeNewGenerator.writeConsumer();
    }

    @Test
    public void writeProvider() {
        queryListAutoCodeNewGenerator.writeProvider();
    }

    @Test
    public void writeDao() {
        queryListAutoCodeNewGenerator.writeDao();
    }

    @Test
    public void writeDaoImpl() {
        queryListAutoCodeNewGenerator.writeDaoImpl();
    }

    @Test
    public void writeMapper() {
        queryListAutoCodeNewGenerator.writeMapper();
    }

    @Test
    public void writeMapperXml() {
        queryListAutoCodeNewGenerator.writeMapperXml();
    }

    @Test
    public void writeClientTest() {
        queryListAutoCodeNewGenerator.writeClientTest();
    }

    @Test
    public void writeServerTest() {
        queryListAutoCodeNewGenerator.writeServerTest();
    }

    @Test
    public void writeTestData() {
        queryListAutoCodeNewGenerator.writeTestData();
    }

    @Test
    public void autoCodeGenerate() {
        queryListAutoCodeNewGenerator.autoCodeGenerate();
    }


    @Test
    public void myTest() {
        System.out.println(transform(FileUtils.readString(this.getClass(), "Controller_Append")));
    }

    private String transform(String template) {
        String methodName = "editExistingCustomers";
        String upperMethodName = upper(methodName);//EditExistingCustomers
        String methodDesc = "存量客户批量导入-存量客户维护";
        String rspName = upperMethodName + "Rsp";//EditExistingCustomersRsp
        String reqName = upperMethodName + "Req";//EditExistingCustomersReq
        String requestModelName = upperMethodName + "Model";//EditExistingCustomersModel
        String requestModelVariableName = "model";
        String consumerName = "publicOfferingCusListConsumer";
//        List<String> fieldNameList = Lists.newArrayList(methodName, rspName, reqName, requestModelName, requestModel, consumerName);

        Map<String, String> fieldNameMap = Maps.newHashMap();
        fieldNameMap.put("methodName", methodName);
        fieldNameMap.put("methodDesc", methodDesc);
        fieldNameMap.put("rspName", rspName);
        fieldNameMap.put("reqName", reqName);
        fieldNameMap.put("requestModelName", requestModelName);
        fieldNameMap.put("requestModelVariableName", requestModelVariableName);
        fieldNameMap.put("consumerName", consumerName);
        for (Map.Entry<String, String> entry : fieldNameMap.entrySet()) {
            template = template.replaceAll(entry.getKey(), entry.getValue());
        }
        return template;
    }
}
