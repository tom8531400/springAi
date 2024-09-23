package com.example.springai01chat.controller;

import com.example.springai01chat.service.MessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping(value = "/meg")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/addMessage", method = RequestMethod.GET)
    public void addMessage() throws Exception {
        try {
            messageService.addMessage();
        } catch (Exception ex) {
            throw new Exception("嘗試連線失敗: " + ex.getMessage());
        }
    }
}
