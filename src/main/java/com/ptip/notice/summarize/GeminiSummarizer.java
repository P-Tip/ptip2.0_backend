package com.ptip.notice.summarize;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GeminiSummarizer {
    private static Client client;

    public GeminiSummarizer(@Value("${gemini.api.key:demo-key}") String apiKey) {
        try {
            this.client = Client.builder()
                    .apiKey(apiKey)
                    .build();
        } catch (Exception e) {
            // API 키가 없거나 잘못된 경우 기본값 사용
            System.out.println("Gemini API 키 설정 실패, 데모 모드로 실행됩니다.");
        }
    }

    public String summarize(String content) {
        try {
            if (client == null) {
                // 데모 모드: 간단한 요약 반환
                return summarizeDemo(content);
            }

            String prompt = "다음 대학 공지사항을 3-5줄로 간결하게 요약해주세요. 중요한 정보(기한, 신청방법, 자격요건 등)를 포함해주세요:\n" + content;

            GenerateContentResponse response = client.models
                    .generateContent("gemini-2.0-flash-exp", prompt, null);

            return response.text();
        } catch (Exception e) {
            e.printStackTrace();
            return summarizeDemo(content);
        }
    }

    private String summarizeDemo(String content) {
        // 데모 모드: 간단한 텍스트 처리
        if (content.length() <= 200) {
            return content;
        }
        return content.substring(0, 200) + "...";
    }
}
