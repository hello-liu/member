package com.moss.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.moss.server.dao")
public class ServerApplication{

    public  static void main(String[] args){
        SpringApplication.run(ServerApplication.class,args);
    }
}
