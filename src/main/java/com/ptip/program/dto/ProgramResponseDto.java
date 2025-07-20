package com.ptip.program.dto;

import com.ptip.program.entity.Program;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProgramResponseDto {

    private int id;
    private String title;
    private String description;
    private String category;
    private String applicationStart;
    private String applicationEnd;
    private String programStart;
    private String programEnd;
    private String mode;
    private String location;
    private String tags;
    private String howToApply;
    private String applyUrl;
    private Boolean liked;

    public static ProgramResponseDto from(Program program) {
        return ProgramResponseDto.builder()
                .id(program.getId())
                .title(program.getTitle())
                .category(program.getCategory())
                .description(program.getDescription())
                .applicationStart(program.getApplicationStart() != null ? program.getApplicationStart().toString() : null)
                .applicationEnd(program.getApplicationEnd() != null ? program.getApplicationEnd().toString() : null)
                .programStart(program.getProgramStart() != null ? program.getProgramStart().toString() : null)
                .programEnd(program.getProgramEnd() != null ? program.getProgramEnd().toString() : null)
                .mode(program.getMode())
                .location(program.getLocation())
                .tags(program.getTags())
                .applyUrl(program.getApplyUrl())
                .howToApply(program.getHowToApply())
                .build();
    }
}
