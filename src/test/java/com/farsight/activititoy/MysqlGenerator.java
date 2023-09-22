package com.farsight.activititoy;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collections;

public class MysqlGenerator {
    private static final String TABLE_NAME = "deadman";
    private static final String PROJECT_PATH = System.getProperty("user.dir");
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/activiti_toy?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true";
    private static final String MYSQL_PWD = "123456";
    private static final String MYSQL_NAME = "root";
    private static final String AUTHOR = "farsight";
    private static final String PARENT = "com.farsight.activititoy";


    public static void main(String[] args) {

        FastAutoGenerator.create(MYSQL_URL, MYSQL_NAME, MYSQL_PWD)
                .globalConfig(builder -> {
                    builder.author(AUTHOR) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .disableOpenDir()
                            .outputDir(PROJECT_PATH + "/src/main/java"); // 指定输出目录)
                })
                .packageConfig(builder -> builder.parent(PARENT)
                        .moduleName("")
                        .entity("entity")
                        .service("service")
                        .serviceImpl("service.impl")
                        .mapper("dao")
                        .xml("dao.xml")
                        .controller("controller")
                        )
                .strategyConfig(builder -> builder
                        //table
                        .enableCapitalMode()
                        .enableSkipView()
                        .disableSqlFilter()
                        .likeTable(new LikeTable("USER"))
                        .addInclude(TABLE_NAME)
                        //entity
                        .entityBuilder()
                        .superClass(Model.class)
                        .disableSerialVersionUID()
                        .enableLombok()
                        .enableRemoveIsPrefix()
                        .enableTableFieldAnnotation()
                        .enableActiveRecord()
                        .naming(NamingStrategy.underline_to_camel)
                        .columnNaming(NamingStrategy.underline_to_camel)
                        .idType(IdType.AUTO)
                        .enableFileOverride()
                        .formatFileName("%s")
                        //service
                        .serviceBuilder()
                        .superServiceClass(IService.class)
                        .superServiceImplClass(ServiceImpl.class)
                        .formatServiceFileName("%sService")
                        .formatServiceImplFileName("%sServiceImp")
                        .enableFileOverride()
                        //mapper
                        .mapperBuilder()
                        .superClass(BaseMapper.class)
                        .enableBaseResultMap()
                        .enableBaseColumnList()
                        .mapperAnnotation(Mapper.class)
                        .formatMapperFileName("%sDao")
                        .enableFileOverride()
                        .formatXmlFileName("%sDao"))
                .templateConfig(builder ->
                        builder.disable(TemplateType.ENTITY)
                                .entity("/templates/entity.java")
                                .service("/templates/service.java")
                                .serviceImpl("/templates/serviceImpl.java")
                                .mapper("/templates/mapper.java")
                                .xml("/templates/mapper.xml")
                                .controller("/templates/controller.java")
                                .build())
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
