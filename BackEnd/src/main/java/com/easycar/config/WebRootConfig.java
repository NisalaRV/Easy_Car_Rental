package com.easycar.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JPAConfig.class)
@ComponentScan(basePackages = "com.easycar.service")

public class WebRootConfig {
    public WebRootConfig() {

        System.out.println("WebRootConfig : Instantiated");
    }
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
