package com.ptip.program.service;

import com.ptip.common.exception.ResourceNotFoundException;
import com.ptip.like.Repository.LikeRepository;
import com.ptip.like.domain.TargetType;
import com.ptip.program.dto.PageResponseDto;
import com.ptip.program.dto.ProgramResponseDto;
import com.ptip.program.entity.Program;
import com.ptip.program.repository.ProgramRepository;
import com.ptip.program.repository.ProgramRepositoryCustom;
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
public class ProgramService {

    private final ProgramRepository programRepository;
    private final ProgramRepositoryCustom programRepositoryCustom;
    private final LikeRepository likeRepository;

    public ProgramService(ProgramRepository programRepository, ProgramRepositoryCustom programRepositoryCustom, LikeRepository likeRepository) {
        this.programRepository = programRepository;
        this.programRepositoryCustom = programRepositoryCustom;
        this.likeRepository = likeRepository;
    }

    public ProgramResponseDto findProgram(int id) {
        Program program = programRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 id의 프로그램을 찾을 수 없습니다."));

        return ProgramResponseDto.from(program);
    }

    public PageResponseDto<ProgramResponseDto> findPrograms(int page, int size, String sort, String keyword, List<String> categories, List<String> modes, List<String> tags, Integer userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        Page<Program> result = programRepositoryCustom.getPrograms(keyword, categories, modes, tags, pageable);

        // 현재 페이지에 있는 프로그램 ID들 추출
        List<Integer> programIds = result.stream()
                .map(Program::getId)
                .toList();

        // 로그인한 사용자가 좋아요한 프로그램 ID를 한 번에 조회
        final Set<Integer> likedProgramIds;
        if (userId != null && !programIds.isEmpty()) {
            likedProgramIds = likeRepository.findAllByUserIdAndTargetTypeAndTargetIdIn(userId, TargetType.교내외, programIds)
                    .stream()
                    .map(like -> like.getTargetId())
                    .collect(Collectors.toSet());
        } else {
            likedProgramIds = Collections.emptySet();
        }

        // DTO 변환 (liked 여부 포함)
        List<ProgramResponseDto> items = result.getContent().stream()
                .map(program -> ProgramResponseDto.of(program, likedProgramIds.contains(program.getId())))
                .toList();

        return PageResponseDto.<ProgramResponseDto>builder()
                .items(items)
                .totalPages(result.getTotalPages())
                .totalElements(result.getTotalElements())
                .currentPage(result.getNumber())
                .pageSize(result.getSize())
                .isLast(result.isLast())
                .build();
    }
}
