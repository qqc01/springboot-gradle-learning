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
    public void test() {
//        System.out.println(queryListAutoCodeNewGenerator.getConfiguration().upperMethodName);
//        System.out.println(queryListAutoCodeNewGenerator.getConfiguration().protoSrcPath);

        String str = "abc{},{}";
        System.out.println(str.replace("{}", "*"));


    }

}
