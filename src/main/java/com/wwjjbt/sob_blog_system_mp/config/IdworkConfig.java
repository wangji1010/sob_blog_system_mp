package com.wwjjbt.sob_blog_system_mp.config;


import com.wwjjbt.sob_blog_system_mp.utils.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdworkConfig {

    @Bean
    public IdWorker createId(){

        return new IdWorker(0,0,0);
    }

}
