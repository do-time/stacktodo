package io.app.stacktodobe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class StacktodoBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(StacktodoBeApplication.class, args);
    }

}
