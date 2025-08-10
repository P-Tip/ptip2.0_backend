package com.ptip.notice.scheduler;

import com.ptip.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NoticeScheduler {

    private final NoticeService noticeService;

    @Scheduled(cron = "0 0 */2 * * *") // 2시간마다 실행
    public void runCrawler() {
        try {
            log.info("공지사항 자동 크롤링 시작");
            noticeService.updateAndSave();
            log.info("공지사항 자동 크롤링 및 요약 완료!");
        } catch (Exception e) {
            log.error("공지사항 크롤링 중 오류 발생: ", e);
        }
    }
}
