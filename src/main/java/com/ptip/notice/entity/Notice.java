package com.ptip.notice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notices", indexes = @Index(columnList = "originId", unique = true))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "origin_id", unique = true, nullable = false)
    private String originId;  // 크롤링된 게시글 PK

    @Column(nullable = false)
    private String title;

    @Column(length = 4000)
    private String summary;

    @Column(nullable = false)
    private String url;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "crawled_at", nullable = false)
    private LocalDateTime crawledAt;

    public Notice(String originId, String title, String summary, String url, LocalDateTime createdAt) {
        this.originId = originId;
        this.title = title;
        this.summary = summary;
        this.url = url;
        this.createdAt = createdAt;
        this.crawledAt = LocalDateTime.now();
    }
}
