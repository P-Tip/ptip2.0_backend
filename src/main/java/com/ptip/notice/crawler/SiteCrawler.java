package com.ptip.notice.crawler;

import com.ptip.notice.dto.NoticeRaw;

import java.util.List;
import java.util.Set;

public interface SiteCrawler {
    List<NoticeRaw> fetchNotices();
    List<NoticeRaw> getNewNotices(Set<String> existingIds);
}
