package com.changgou;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "com.changgou.auth.dao")
@EnableFeignClients(basePackages ={"com.changgou.user.feign"} )
public class OauthApplication {
    public static void main(String[] args) {

        SpringApplication.run(OauthApplication.class, args);
        // System.out.println("Hello world!");
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}