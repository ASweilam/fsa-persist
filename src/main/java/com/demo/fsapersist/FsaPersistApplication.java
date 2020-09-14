package com.demo.fsapersist;

import com.demo.fsapersist.services.EstablishmentDetailService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FsaPersistApplication {

    public static void main(String[] args) {
//        SpringApplication.run(FsaPersistApplication.class, args);
        ConfigurableApplicationContext ctx = SpringApplication.run(FsaPersistApplication.class, args);


    }

    @Bean
    CommandLineRunner runner(EstablishmentDetailService establishmentDetailService) {

        return args -> {
           establishmentDetailService.persist();
        };

    }

}
