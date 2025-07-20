package com.ptip.program.controller;

import com.ptip.common.dto.ApiResponse;
import com.ptip.program.dto.PageResponseDto;
import com.ptip.program.dto.ProgramResponseDto;
import com.ptip.program.dto.ScholarshipResponseDto;
import com.ptip.program.service.ProgramService;
import com.ptip.program.service.ScholarshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programs")
@Tag(name = "장학금 및 교내외 프로그램", description = "장학금 및 교내외 프로그램 조회 API")
public class ProgramController {

    private final ScholarshipService scholarshipService;
    private final ProgramService programService;

    public ProgramController(ScholarshipService scholarshipService, ProgramService programService) {
        this.scholarshipService = scholarshipService;
        this.programService = programService;
    }

    @Operation(summary = "단일 장학금 프로그램 조회", description = "클라이언트가 조회할 데이터를 {id}로 구분해서 요청해주면 해당 id의 장학금 프로그램을 반환합니다.")
    @GetMapping("/scholarship/{id}")
    public ResponseEntity<ApiResponse<ScholarshipResponseDto>> getScholarship(@PathVariable int id) {
        return ApiResponse.success(scholarshipService.findScholarship(id));
    }

    @Operation(summary = "조건 검색된 장학금 프로그램 목록 조회", description = "키워드(keyword)와 정렬 방식(sort), 필터 조건(amount, status)으로 장학금 프로그램을 검색하며, 페이징 처리를 진행하여 데이터 목록을 반환합니다.\n" +
            "정렬 방식(sort)에는 [마감일순, 금액순, 인기순] 세가지가 있으며, 기본값은 마감일순입니다.\n" +
            "필터 조건(amount)에는 [5미만, 5~10, 10이상] 세가지가 있으며 기본값은 전체 표시입니다. 필터 조건(status)에는 [진행중, 마감임박, 마감] 세가지가 있으며 기본값은 전체 표시입니다. 마감임박일 때에는 limit를 통해 임박 기준을 정할 수 있으며 기본값은 4일입니다.")
    @GetMapping("/scholarship")
    public ResponseEntity<ApiResponse<PageResponseDto<ScholarshipResponseDto>>> getScholarshipList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "deadline") String sort,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String amount,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) int limit
    ) {
        PageResponseDto<ScholarshipResponseDto> response = scholarshipService.findScholarships(page, size, sort, keyword, amount, status, limit);
        return ApiResponse.success(response);
    }

    @Operation(summary = "단일 교내외 프로그램 조회", description = "클라이언트가 조회할 데이터를 {id}로 구분해서 요청해주면 해당 id의 교내외 프로그램을 반환합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProgramResponseDto>> getProgram(@PathVariable int id) {
        return ApiResponse.success(programService.findProgram(id));
    }

    @Operation(summary = "조건 검색된 교내외 프로그램 목록 조회", description = "키워드(keyword)와 정렬 방식(sort), 필터 조건(category, mode, tag)으로 교내외 프로그램을 검색하며, 페이징 처리를 진행하여 데이터 목록을 반환합니다.\n" +
            "정렬 방식(sort)에는 마감임박순, 최신순, 인기순 세가지가 있으며, 기본값은 마감임박순입니다.\n" +
            "필터 조건(category)에는 [학술, 취업, 문화, 봉사, 국제교류, 창업] 여섯가지가 있으며(다중 선택 가능) 기본값은 전체 표시입니다. 필터 조건(mode)에는 [오프라인, 온라인, 혼합] 세가지가 있으며(다중 선택 가능) 기본값은 전체 표시입니다." +
            "필터 조건(tag)에는 [장학금, 인증서, 학점, 상금, 멘토링] 다섯가지가 있으며(다중 선택 가능) 기본값은 전체 표시입니다.")
    @GetMapping("/general")
    public ResponseEntity<ApiResponse<PageResponseDto<ProgramResponseDto>>> getProgramList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "applicationEnd") String sort,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<String> category,
            @RequestParam(required = false) List<String> mode,
            @RequestParam(required = false) List<String> tag
    ) {
        PageResponseDto<ProgramResponseDto> response = programService.findPrograms(page, size, sort, keyword, category, mode, tag);
        return ApiResponse.success(response);
    }

}
