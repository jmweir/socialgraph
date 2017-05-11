package com.jweir.socialgraph;

import com.jweir.socialgraph.config.seed.EnableDataSeed;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableMongoRepositories("com.jweir.socialgraph.repository")
@EnableSpringDataWebSupport
@EnableDataSeed
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
