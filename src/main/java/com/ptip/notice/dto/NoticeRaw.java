package com.ptip.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NoticeRaw {
    private String originId;
    private String title;
    private String content;
    private String url;
}
