package com.ptip.program.repository;

import com.ptip.program.entity.Scholarship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ScholarshipRepositoryCustom {
    Page<Scholarship> getScholarships(String query, String amount, String status, int limit, Pageable pageable);
}
