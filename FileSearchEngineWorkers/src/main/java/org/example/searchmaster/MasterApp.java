package org.example.searchmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MasterApp {
    public static void main(String[] args){

        SpringApplication.run(MasterApp.class, args);
    }
}
