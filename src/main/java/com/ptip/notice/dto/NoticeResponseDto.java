package com.ptip.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NoticeResponseDto {
    private Long id;
    private String title;
    private String summary;
    private String url;
    private LocalDateTime createdAt;
    private LocalDateTime crawledAt;
}
