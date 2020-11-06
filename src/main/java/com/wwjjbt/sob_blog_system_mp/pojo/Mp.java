package com.wwjjbt.sob_blog_system_mp.pojo;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

public class Mp {
    public static void main(String[] args) {
        //需要构建一个代码生成器对象
        AutoGenerator mpg = new AutoGenerator();
        //配置策略

        //1、全局配置
        GlobalConfig gc = new GlobalConfig();
        String prPath = System.getProperty("user.dir");//获取当前系统目录
        gc.setOutputDir(prPath+"/src/main/java");//指定输出的位置
        gc.setAuthor("wangji");//设置作者
        gc.setOpen(false);//是否打开资源管理器
        gc.setFileOverride(false);//是否覆盖原来的文件
        gc.setServiceName("%sService");//去掉service的i前缀
        gc.setIdType(IdType.ID_WORKER);//设置id的生成策略默认算法
        gc.setDateType(DateType.ONLY_DATE);//设置日期生成策略
        gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);

        //2、设置数据源”
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/sob_blog_system?serverTimezone=UTC");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setDbType(DbType.MYSQL);//数据库类型
        mpg.setDataSource(dsc);

        //3、配置包
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("sob_blog_system_mp");//设置模块
        pc.setParent("com.wwjjbt");
        pc.setEntity("pojo");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setController("controller");
        mpg.setPackageInfo(pc);


//        4、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("tb_lost_image");//指定要映射的数据库表，可以写多个
        strategy.setNaming(NamingStrategy.underline_to_camel);//设置命名规则下划线转驼峰
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);//列名规则
        strategy.setEntityLombokModel(true);//是否生成lombok注解

//        strategy.setLogicDeleteFieldName("deleted");//逻辑删除字段配置
        //自动填充的配置
        TableFill create_time = new TableFill("create_time", FieldFill.INSERT);//设置时的生成策略
        TableFill update_time = new TableFill("update_time", FieldFill.INSERT_UPDATE);//设置更新时间的生成策略
        ArrayList<TableFill> list = new ArrayList<>();
        list.add(create_time);
        list.add(update_time);
        strategy.setTableFillList(list);

        //乐观锁
//        strategy.setVersionFieldName("version");
        strategy.setRestControllerStyle(true);//开启驼峰命名
//        strategy.setControllerMappingHyphenStyle(true);//开启链接地址的下划线命名 localhost:8080/hello_id_2
        mpg.setStrategy(strategy);

        mpg.execute();//执行
    }

}
