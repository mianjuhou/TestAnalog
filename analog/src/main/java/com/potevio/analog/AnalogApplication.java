package com.potevio.analog;

import com.potevio.analog.util.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AnalogApplication {

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }

    public static void main(String[] args) {
        SpringApplication.run(AnalogApplication.class, args);
    }

}
