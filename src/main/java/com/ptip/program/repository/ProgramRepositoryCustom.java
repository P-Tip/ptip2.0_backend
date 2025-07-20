package com.ptip.program.repository;

import com.ptip.program.entity.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramRepositoryCustom {
    Page<Program> getPrograms(String keyword, List<String> categories, List<String> modes, List<String> tags, Pageable pageable);
}
