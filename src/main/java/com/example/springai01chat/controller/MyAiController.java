package com.example.springai01chat.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.ai.embedding.*;
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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private OpenAiEmbeddingModel openAiEmbeddingModel; // 匹配文字

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

    @RequestMapping(value = "/getEmbedding1", method = RequestMethod.GET)
    public EmbeddingResponse getEmbedding1(@RequestParam String text1) {
        try {
            List<String> data = new ArrayList<>();
            data.add(text1);
            EmbeddingOptions embeddingOptions = EmbeddingOptionsBuilder.builder()
                    .withModel("text-embedding-ada-002") // 使用你指定的模型
                    .build();
            EmbeddingRequest request = new EmbeddingRequest(data, embeddingOptions);
            return openAiEmbeddingModel.call(request);
        } catch (Exception ex) {
            log.error("調用 OpenAI 出現錯誤: {}", ex.getMessage(), ex);
        }
        return null;
    }

    @RequestMapping(value = "/getEmbedding2", method = RequestMethod.GET)
    public Double getEmbedding2(@RequestParam String text1, @RequestParam String text2) {
        try {
            List<String> data = new ArrayList<>();
            data.add(text1);
            data.add(text2);
            EmbeddingOptions embeddingOptions = EmbeddingOptionsBuilder.builder()
                    .withModel("text-embedding-ada-002") // 使用你指定的模型
                    .build();
            EmbeddingRequest request = new EmbeddingRequest(data, embeddingOptions);
            EmbeddingResponse response = openAiEmbeddingModel.call(request);

            // 提取兩個文本的嵌入向量
            List<Embedding> results = response.getResults();// 使用 getResults() 獲取嵌入向量列表
            float[] embedding1 = results.get(0).getOutput();  // 第一個文本的嵌入
            float[] embedding2 = results.get(1).getOutput();
            // 計算兩個嵌入向量之間的相似度
            return calculateCosineSimilarity(embedding1, embedding2);
        } catch (Exception ex) {
            log.error("調用 OpenAI 出現錯誤: {}", ex.getMessage(), ex);
        }
        return null;
    }


    @RequestMapping(value = "/getEmbedding3", method = RequestMethod.GET)
    public String getEmbedding3(@RequestParam(value = "text1") String text1) {
        try {
            List<String> categories = new ArrayList<>();
            categories.add("汽車類");
            categories.add("動物類");
            categories.add("食物類");


            List<String> data = new ArrayList<>();
            data.add(text1);
            data.addAll(categories);

            EmbeddingOptions embeddingOptions = EmbeddingOptionsBuilder.builder()
                    .withModel("text-embedding-ada-002") // 使用你指定的模型
                    .build();
            EmbeddingRequest request = new EmbeddingRequest(data, embeddingOptions);
            EmbeddingResponse response = openAiEmbeddingModel.call(request);

            List<Embedding> results = response.getResults();
            float[] embedding1 = results.get(0).getOutput();
            float[] embedding2 = results.get(1).getOutput();
            float[] embedding3 = results.get(2).getOutput();
            float[] embedding4 = results.get(3).getOutput();

            double car = calculateCosineSimilarity(embedding1, embedding2);
            double animal = calculateCosineSimilarity(embedding1, embedding3);
            double food = calculateCosineSimilarity(embedding1, embedding4);
            String endType = "未知";
            double max = Math.max(car, Math.max(animal, food));
            if (max == car) {
                endType = "1";
            } else if (max == animal) {
                endType = "2";
            } else if (max == food) {
                endType = "3";
            }
            return endType;
        } catch (Exception ex) {
            log.error("調用 OpenAI 出現錯誤: {}", ex.getMessage(), ex);
        }
        return null;
    }

    @RequestMapping(value = "/getEmbedding4", method = RequestMethod.GET)
    public String getEmbedding4(@RequestParam(value = "text") String text) throws IOException {
        double maxSimilarity = -1;
        String mostSimilarText = null;
        Path path = Paths.get("demo.txt");
        List<String> list = Files.readAllLines(path);
        list = list.stream().filter(o -> !"".equals(o)).collect(Collectors.toList());

        List<String> fileList = new ArrayList<>(list);
        fileList.add(0, text);

        EmbeddingOptions embeddingOptions = EmbeddingOptionsBuilder.builder()
                .withModel("text-embedding-ada-002") // 使用你指定的模型
                .build();
        EmbeddingRequest request = new EmbeddingRequest(fileList, embeddingOptions);
        EmbeddingResponse response = openAiEmbeddingModel.call(request);

        List<Embedding> results = response.getResults();
        float[] textData = results.get(0).getOutput();
        for (int i = 1; i < results.size(); i++) {
            float[] temp = results.get(i).getOutput();
            double d = calculateCosineSimilarity(textData, temp);
            if (d > maxSimilarity) {
                maxSimilarity = d;
                mostSimilarText = fileList.get(i);
                System.out.println(">>>>>>>>>>>>>>>>>>" + mostSimilarText);
            }
        }
        return mostSimilarText;
    }


    // 計算餘弦相似度的方法
    private double calculateCosineSimilarity(float[] vectorA, float[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];  // 點積
            normA += Math.pow(vectorA[i], 2);       // 向量 A 的範數
            normB += Math.pow(vectorB[i], 2);       // 向量 B 的範數
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));  // 餘弦相似度
    }

}