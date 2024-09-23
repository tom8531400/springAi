package com.example.springai01chat.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.time.Duration;

@Log4j2
@RestController
@RequestMapping(value = "/api")
public class MyAiController {

    // spring-ai 自動裝配可以直接配置使用
    @Autowired
    private OpenAiChatModel openAiChatModel; // 文字聊天
    @Autowired
    private OpenAiImageModel openAiImageModel; // 生成圖片
    @Autowired
    private OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel; // 語音文字轉換
    @Autowired
    private OpenAiAudioSpeechModel openAiAudioSpeechModel; // 文字轉語音

    /**
     * 文字聊天(同步 call)
     *
     * @param question 用戶輸入問題
     * @return openai回答
     */
    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public String chat(@RequestParam(value = "question") String question) {
        String response = "";
        log.info("用戶的問題是: {}", question);
        try {
            response = openAiChatModel.call(question); // 調用方法call
            log.info("AI的回覆是: {}", response);
            return response;
        } catch (Exception ex) {
            log.info("調用openai錯誤訊息: {}", ex.getMessage());
            ex.printStackTrace();
        }
        return response;
    }


    /**
     * 文字聊天(流式 stream)
     *
     * @param question 用戶輸入問題
     * @return openai回答
     */
    @RequestMapping(value = "/stream", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestParam(value = "question") String question) {
        Flux<String> response = null;
        log.info("用戶的問題是: {}", question);
        try {
            response = openAiChatModel.stream(question);
            return response.delayElements(Duration.ofSeconds(1))
                    .map(o -> o + " ");
        } catch (Exception ex) {
            log.info("調用openai錯誤訊息: {}", ex.getMessage());
            ex.printStackTrace();
        }
        return response;
    }

    /**
     * 生成圖片
     *
     * @param question 用戶輸入需求
     * @return openai生成圖片
     */
    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public String image(@RequestParam(value = "question") String question) {
        log.info("用戶想生成的圖片是: {}", question);
        try {
            ImagePrompt prompt = new ImagePrompt(question, OpenAiImageOptions.builder()
                    .withQuality("hd") // 高清
                    .withN(1) // 生成一張
                    .withHeight(1024) // 高度
                    .withWidth(1024) // 寬度
                    .build());
            ImageResponse imageResponse = openAiImageModel.call(prompt);
            log.info("response: {}", imageResponse);
            return imageResponse.getResult().getOutput().getUrl().replace(" ", "");
        } catch (Exception ex) {
            log.info("調用openai錯誤訊息: {}", ex.getMessage());
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 聲音轉文字
     *
     * @param file 用戶傳入mp3檔案
     * @return openai轉文字
     */
    @RequestMapping(value = "/audioTotext", method = RequestMethod.POST)
    public String audioTotext(@RequestParam(value = "file") MultipartFile file) throws Exception {
        log.info("用戶想轉換的音檔: {}", file);
        if (file.isEmpty()) {
            throw new Exception("檔案錯誤");
        }
        try {
            File tempFile = File.createTempFile("uploaded-audio", ".mp3");
            file.transferTo(tempFile);
            Resource audioResource = new FileSystemResource(tempFile);
            log.info("用戶上傳檔案: {}", audioResource);
            return openAiAudioTranscriptionModel.call(audioResource);
        } catch (Exception ex) {
            log.info("調用openai錯誤訊息: {}", ex.getMessage());
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 文字轉聲音
     *
     * @param text 用戶輸入文字
     * @param file 用戶上傳文件
     * @return ai轉換語音
     */
    @RequestMapping(value = "/textToaudio", method = RequestMethod.POST)
    public Object textToaudio(@RequestParam(value = "text", required = false) String text,
                              @RequestParam(value = "file", required = false) MultipartFile file) {
        byte[] bytes = null;
        String request = "";
        if (text != null && !text.isEmpty()) {
            request = text;
        } else if (file != null) {
            request = getFileStr(file);
        }
        log.info("用戶想轉換的文字: {}", request);
        try {
            bytes = openAiAudioSpeechModel.call(request);
            File file2 = new File("temp/" + System.currentTimeMillis() + ".mp3");
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            return bytes;
        } catch (Exception ex) {
            log.info("調用openai錯誤訊息: {}", ex.getMessage());
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * MultipartFile 轉換成字串
     *
     * @param file MultipartFile
     * @return 轉換後字串
     */
    private static String getFileStr(MultipartFile file) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString();
        } catch (Exception ex) {
            log.info("調用openai錯誤訊息: {}", ex.getMessage());
            ex.printStackTrace();
        }
        return stringBuilder.toString();
    }
}