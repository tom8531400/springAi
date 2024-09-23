package com.example.springai01chat.service.Impl;

import com.example.springai01chat.config.netty.Client.NettyClient;
import com.example.springai01chat.service.MessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class MessageServiceImpl implements MessageService {
    @Override
    public void addMessage() throws Exception {
        log.info("客戶端: {} 準備連線...", Thread.currentThread().getName());
        try {
            NettyClient.start();
        } catch (Exception ex) {
            throw new Exception("加入連線失敗: " + ex.getMessage());
        }
    }
}
