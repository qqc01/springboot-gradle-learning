package com.qqc.apexsoft.codegenerator.dataImport;

import com.qqc.apexsoft.codegenerator.config.AutoCodeGeneratorConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.LinkedList;

import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = AutoCodeGeneratorConfig.class)
public class DataImportAutoCodeGeneratorTest {
    public static final Logger log = LoggerFactory.getLogger(DataImportAutoCodeGeneratorTest.class);
    @Autowired
    private DataImportAutoCodeGenerator dataImportAutoCodeGenerator;

    @Test
    public void contextLoads() {
        System.out.println(dataImportAutoCodeGenerator);
    }

    @Test
    public void writeProto() {
        dataImportAutoCodeGenerator.writeProto();
    }

    @Test
    public void writeModel() {
        dataImportAutoCodeGenerator.writeModel();
    }

    @Test
    public void writeBean() {
        dataImportAutoCodeGenerator.writeBean();
    }

    @Test
    public void writeController() {
        dataImportAutoCodeGenerator.writeController();
    }

    @Test
    public void writeConsumer() {
        dataImportAutoCodeGenerator.writeConsumer();
    }

    @Test
    public void writeProvider() {
        dataImportAutoCodeGenerator.writeProvider();
    }

    @Test
    public void writeDao() {
        dataImportAutoCodeGenerator.writeDao();
    }

    @Test
    public void writeDaoImpl() {
        dataImportAutoCodeGenerator.writeDaoImpl();
    }

    @Test
    public void writeMapper() {
        dataImportAutoCodeGenerator.writeMapper();
    }

    @Test
    public void writeMapperXml() {
        dataImportAutoCodeGenerator.writeMapperXml();
    }

    @Test
    public void autoCodeGenerate() {
        dataImportAutoCodeGenerator.autoCodeGenerate();
    }
}
