package com.dairy.take12;

import com.dairy.take12.model.TwilioConfig;
import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.dairy.take12.repository")
public class Take12Application {

    public static void main(String[] args) {
        SpringApplication.run(Take12Application.class, args);
    }
}
