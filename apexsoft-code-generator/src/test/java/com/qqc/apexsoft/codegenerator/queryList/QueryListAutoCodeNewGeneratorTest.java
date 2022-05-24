package com.qqc.apexsoft.codegenerator.queryList;

import com.qqc.apexsoft.codegenerator.config.AutoCodeGeneratorConfig;
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

@SpringBootTest
@ContextConfiguration(classes = AutoCodeGeneratorConfig.class)
public class QueryListAutoCodeNewGeneratorTest {
    public static final Logger log = LoggerFactory.getLogger(QueryListAutoCodeNewGeneratorTest.class);
    @Autowired
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

}
