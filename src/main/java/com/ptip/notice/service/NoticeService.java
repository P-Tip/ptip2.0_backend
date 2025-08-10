package com.ptip.notice.service;

import com.ptip.notice.crawler.SiteCrawler;
import com.ptip.notice.dto.NoticeRaw;
import com.ptip.notice.dto.NoticeResponseDto;
import com.ptip.notice.entity.Notice;
import com.ptip.notice.repository.NoticeRepository;
import com.ptip.notice.summarize.GeminiSummarizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final List<SiteCrawler> crawlers;
    private final NoticeRepository repository;
    private final GeminiSummarizer summarizer;

    public void updateAndSave() {
        Set<String> existingIds = repository.findAll().stream()
                .map(Notice::getOriginId)
                .collect(Collectors.toSet());

        for (SiteCrawler crawler : crawlers) {
            List<NoticeRaw> newNotices = crawler.getNewNotices(existingIds);
            List<Notice> noticesToSave = newNotices.stream()
                    .map(this::convertToNotice)
                    .collect(Collectors.toList());
            
            if (!noticesToSave.isEmpty()) {
                repository.saveAll(noticesToSave);
                System.out.println("새로운 공지사항 " + noticesToSave.size() + "개 저장 완료");
            }
        }
    }

    private Notice convertToNotice(NoticeRaw raw) {
        String summary = summarizer.summarize(raw.getContent());
        return new Notice(
                raw.getOriginId(),
                raw.getTitle(),
                summary,
                raw.getUrl(),
                LocalDateTime.now()
        );
    }

    public List<NoticeResponseDto> getAllNotices() {
        return repository.findAllOrderByCreatedAtDesc().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public List<NoticeResponseDto> searchNotices(String keyword) {
        return repository.findByKeyword(keyword).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    public NoticeResponseDto getNoticeById(Long id) {
        Notice notice = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));
        return convertToResponseDto(notice);
    }

    private NoticeResponseDto convertToResponseDto(Notice notice) {
        return new NoticeResponseDto(
                notice.getId(),
                notice.getTitle(),
                notice.getSummary(),
                notice.getUrl(),
                notice.getCreatedAt(),
                notice.getCrawledAt()
        );
    }
}
