package com.ptip.notice.crawler;

import com.ptip.notice.dto.NoticeRaw;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class NoticeCrawler implements SiteCrawler {

    @Override
    public List<NoticeRaw> fetchNotices() {
        List<NoticeRaw> list = new ArrayList<>();
        try {
            // PTU 대학 공지사항 페이지 크롤링
            String baseUrl = "https://www.ptu.ac.kr/bbs/www/310/32031/artclView.do";
            Document doc = Jsoup.connect(baseUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(10000)
                    .get();

            // 공지사항 목록에서 최신 게시글들 추출
            // 실제 구현에서는 여러 페이지를 순회하며 크롤링
            String originId = "32031"; // 게시글 PK
            String title = doc.selectFirst(".view-title") != null ? 
                doc.selectFirst(".view-title").text().trim() : "제목 없음";
            String content = doc.selectFirst(".view-con") != null ? 
                doc.selectFirst(".view-con").html() : "";

            // HTML 태그 정리
            if (!content.isEmpty()) {
                content = content.replaceAll("(?i)<br\\s*/?>", "\n")
                        .replaceAll("(?i)</p>", "\n")
                        .replaceAll("&nbsp;", " ")
                        .replaceAll("<[^>]*>", "")
                        .trim();
            }

            list.add(new NoticeRaw(originId, title, content, baseUrl));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<NoticeRaw> getNewNotices(Set<String> existingIds) {
        List<NoticeRaw> allNotices = fetchNotices();
        return allNotices.stream()
                .filter(notice -> !existingIds.contains(notice.getOriginId()))
                .collect(Collectors.toList());
    }
}
