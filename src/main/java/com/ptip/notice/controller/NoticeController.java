package com.ptip.notice.controller;

import com.ptip.common.dto.ApiResponse;
import com.ptip.notice.dto.NoticeResponseDto;
import com.ptip.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@Tag(name = "학교 공지사항", description = "학교 공지사항 조회 및 검색 API")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "전체 공지사항 조회", description = "최신순으로 정렬된 모든 공지사항을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<NoticeResponseDto>>> getAllNotices() {
        List<NoticeResponseDto> notices = noticeService.getAllNotices();
        return ApiResponse.success(notices);
    }

    @Operation(summary = "공지사항 검색", description = "키워드로 공지사항을 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<NoticeResponseDto>>> searchNotices(
            @RequestParam String keyword) {
        List<NoticeResponseDto> notices = noticeService.searchNotices(keyword);
        return ApiResponse.success(notices);
    }

    @Operation(summary = "단일 공지사항 조회", description = "ID로 특정 공지사항을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NoticeResponseDto>> getNoticeById(@PathVariable Long id) {
        NoticeResponseDto notice = noticeService.getNoticeById(id);
        return ApiResponse.success(notice);
    }

    @Operation(summary = "수동 크롤링 실행", description = "관리자용 - 수동으로 크롤링을 실행합니다.")
    @PostMapping("/crawl")
    public ResponseEntity<ApiResponse<String>> runCrawler() {
        noticeService.updateAndSave();
        return ApiResponse.success("크롤링이 완료되었습니다.");
    }
}
