package net.artux.sendler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Configuration
public class SendlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SendlerApplication.class, args);
    }

}