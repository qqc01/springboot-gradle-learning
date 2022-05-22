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
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        queryListAutoCodeNewGenerator.fileCheck();
        this.queryListAutoCodeNewGenerator = queryListAutoCodeNewGenerator;
    }
}
