package com.remnant.orderservice;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties(ApplicationProperties.class)
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    //	@Bean
    //	CommandLineRunner commandLineRunner(Environment env) {
    //		return args -> {
    //			System.out.println("RabbitMQ Host: " + env.getProperty("spring.rabbitmq.host"));
    //			System.out.println("RabbitMQ Port: " + env.getProperty("spring.rabbitmq.port"));
    //			System.out.println("RabbitMQ Username: " + env.getProperty("spring.rabbitmq.username"));
    //			System.out.println("RabbitMQ Virtual Host: " + env.getProperty("spring.rabbitmq.virtual-host"));
    //		};
    //	}

}
