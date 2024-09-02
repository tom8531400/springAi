package com.example.springai01chat.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping(value = "/api")
public class MyAiController {
    @Autowired
    private OpenAiChatModel openAiChatModel;

    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public String chat(@RequestParam(value = "question") String question) {
        log.info("用戶的問題是: {}", question);
        System.out.println("問題 : " + question);
        return openAiChatModel.call(question);
    }
}
