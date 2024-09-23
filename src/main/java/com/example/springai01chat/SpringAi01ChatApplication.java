package com.example.springai01chat;

import com.example.springai01chat.config.netty.NettyConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringAi01ChatApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringAi01ChatApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
