package com.ptip.program.service;

import com.ptip.common.exception.ResourceNotFoundException;
import com.ptip.like.Repository.LikeRepository;
import com.ptip.like.domain.TargetType;
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

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScholarshipService {

    private final ScholarshipRepository scholarshipRepository;
    private final ScholarshipRepositoryCustom scholarshipRepositoryCustom;
    private final LikeRepository likeRepository;

    public ScholarshipService(ScholarshipRepository scholarshipRepository, ScholarshipRepositoryCustom scholarshipRepositoryCustom, LikeRepository likeRepository) {
        this.scholarshipRepository = scholarshipRepository;
        this.scholarshipRepositoryCustom = scholarshipRepositoryCustom;
        this.likeRepository = likeRepository;
    }

    public ScholarshipResponseDto findScholarship(int id) {
        Scholarship scholarship = scholarshipRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 id의 장학금 프로그램을 찾을 수 없습니다."));

        return ScholarshipResponseDto.from(scholarship);
    }

    public PageResponseDto<ScholarshipResponseDto> findScholarships(int page, int size, String sort, String keyword, String amount, String status, int limit, Integer userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        Page<Scholarship> result = scholarshipRepositoryCustom.getScholarships(keyword, amount, status, limit, pageable);

        // 현재 페이지에 있는 프로그램 ID들 추출
        List<Integer> scholarshipIds = result.stream()
                .map(Scholarship::getId)
                .toList();

        // 로그인한 사용자가 좋아요한 프로그램 ID를 한 번에 조회
        final Set<Integer> likedScholarshipIds;
        if (userId != null && !scholarshipIds.isEmpty()) {
            likedScholarshipIds = likeRepository.findAllByUserIdAndTargetTypeAndTargetIdIn(userId, TargetType.장학금, scholarshipIds)
                    .stream()
                    .map(like -> like.getTargetId())
                    .collect(Collectors.toSet());
        } else {
            likedScholarshipIds = Collections.emptySet();
        }

        // DTO 변환 (liked 여부 포함)
        List<ScholarshipResponseDto> items = result.getContent().stream()
                .map(scholarship -> ScholarshipResponseDto.of(scholarship, likedScholarshipIds.contains(scholarship.getId())))
                .toList();

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
