package com.ptip.notice.repository;

import com.ptip.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    
    Optional<Notice> findByOriginId(String originId);
    
    @Query("SELECT n FROM Notice n ORDER BY n.createdAt DESC")
    List<Notice> findAllOrderByCreatedAtDesc();
    
    @Query("SELECT n FROM Notice n WHERE n.title LIKE %:keyword% OR n.summary LIKE %:keyword% ORDER BY n.createdAt DESC")
    List<Notice> findByKeyword(@Param("keyword") String keyword);
    
    boolean existsByOriginId(String originId);
}
