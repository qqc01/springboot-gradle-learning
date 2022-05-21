package com.qqc.apexsoft.codegenerator.common;

public interface AutoCodeGenerator {
    default void autoCodeGenerate() {
        writeProto();
        writeModel();
        writeController();
        writeConsumer();
        writeProvider();
        writeDao();
        writeDaoImpl();
        writeMapper();
        writeMapperXml();
        writeClientTest();
        writeServerTest();
        writeTestData();
    }
    void config();
    void writeProto();
    void writeModel();
    void writeController();
    void writeConsumer();
    void writeProvider();
    void writeDao();
    void writeDaoImpl();
    void writeMapper();
    void writeMapperXml();
    void writeClientTest();
    void writeServerTest();
    void writeTestData();
}
