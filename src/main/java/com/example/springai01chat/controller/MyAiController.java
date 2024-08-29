package com.example.springai01chat.controller;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyAiController {
    @Autowired
    private OpenAiChatModel openAiChatModel;

    @RequestMapping(value = "/api/chat", method = RequestMethod.GET)
    public String chat(@RequestParam(value = "msg") String msg) {
        System.out.println("問題 : " + msg);

        return openAiChatModel.call(msg);
    }
}
