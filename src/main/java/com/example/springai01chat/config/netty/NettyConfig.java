package com.example.springai01chat.config.netty;

import com.example.springai01chat.config.netty.Server.NettyServer;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyConfig {

    @Bean
    public NettyServer nettyServer() {
        return new NettyServer(9111);
    }

    @Bean
    public CommandLineRunner runner(NettyServer nettyServer) {
        return args -> nettyServer.start();
    }
}
