package com.ptip.program.service;

import com.ptip.common.exception.ResourceNotFoundException;
import com.ptip.program.dto.PageResponseDto;
import com.ptip.program.dto.ScholarshipResponseDto;
import com.ptip.program.entity.Scholarship;
import com.ptip.program.repository.ScholarshipRepository;
import com.ptip.program.repository.ScholarshipRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScholarshipService {

    private final ScholarshipRepository scholarshipRepository;
    private final ScholarshipRepositoryCustom scholarshipRepositoryCustom;

    public ScholarshipService(ScholarshipRepository scholarshipRepository, ScholarshipRepositoryCustom scholarshipRepositoryCustom) {
        this.scholarshipRepository = scholarshipRepository;
        this.scholarshipRepositoryCustom = scholarshipRepositoryCustom;
    }

    public ScholarshipResponseDto findScholarship(int id) {
        Scholarship scholarship = scholarshipRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 id의 장학금 프로그램을 찾을 수 없습니다."));

        return ScholarshipResponseDto.from(scholarship);
    }

    public PageResponseDto<ScholarshipResponseDto> findScholarships(int page, int size, String sort, String keyword, String amount, String status, int limit) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        Page<Scholarship> result = scholarshipRepositoryCustom.getScholarships(keyword, amount, status, limit, pageable);

        List<ScholarshipResponseDto> items = result.getContent().stream()
                .map(ScholarshipResponseDto::from)
                .collect(Collectors.toList());

        return PageResponseDto.<ScholarshipResponseDto>builder()
                .items(items)
                .totalPages(result.getTotalPages())
                .totalElements(result.getTotalElements())
                .currentPage(result.getNumber())
                .pageSize(result.getSize())
                .isLast(result.isLast())
                .build();
    }
}
