package de.klopfdreh.ai.tests;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class AIApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AIApplication.class).run(args);
    }

}
