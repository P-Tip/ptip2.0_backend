package com.ptip.program.service;

import com.ptip.common.exception.ResourceNotFoundException;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;
    private final ProgramRepositoryCustom programRepositoryCustom;

    public ProgramService(ProgramRepository programRepository, ProgramRepositoryCustom programRepositoryCustom) {
        this.programRepository = programRepository;
        this.programRepositoryCustom = programRepositoryCustom;
    }

    public ProgramResponseDto findProgram(int id) {
        Program program = programRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 id의 프로그램을 찾을 수 없습니다."));

        return ProgramResponseDto.from(program);
    }

    public PageResponseDto<ProgramResponseDto> findPrograms(int page, int size, String sort, String keyword, List<String> categories, List<String> modes, List<String> tags) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        Page<Program> result = programRepositoryCustom.getPrograms(keyword, categories, modes, tags, pageable);

        List<ProgramResponseDto> items = result.getContent().stream()
                .map(ProgramResponseDto::from)
                .collect(Collectors.toList());

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
