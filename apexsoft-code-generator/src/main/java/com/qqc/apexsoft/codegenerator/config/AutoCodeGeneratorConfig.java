package com.qqc.apexsoft.codegenerator.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(value = "com.qqc.apexsoft.codegenerator")
@EnableAspectJAutoProxy(proxyTargetClass =true)
public class AutoCodeGeneratorConfig {

}
