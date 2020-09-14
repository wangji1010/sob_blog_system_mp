package com.wwjjbt.sob_blog_system_mp;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Random;


@SpringBootApplication

@MapperScan("com.wwjjbt.sob_blog_system_mp.mapper")
//@EnableCaching //开启缓存  使用redis作为缓存中间件
public class SobBlogSystemMpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SobBlogSystemMpApplication.class, args);
    }

        @Bean
        public Random random(){

        return new Random();
        }
        @Bean
    public ISqlInjector sqlInjector(){
        return new LogicSqlInjector();
        }

}


