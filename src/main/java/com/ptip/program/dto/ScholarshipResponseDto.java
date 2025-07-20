package com.ptip.program.dto;

import com.ptip.program.entity.Scholarship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ScholarshipResponseDto {

    private int id;
    private String title;
    private String description;
    private String department;
    private int minAmount;
    private int maxAmount;
    private String deadline;
    private String eligibility;
    private String requiredDocuments;
    private String steps;
    private String applyUrl;

    public static ScholarshipResponseDto from(Scholarship scholarship) {
        return ScholarshipResponseDto.builder()
                .id(scholarship.getId())
                .title(scholarship.getTitle())
                .description(scholarship.getDescription())
                .department(scholarship.getDepartment())
                .minAmount(scholarship.getMinAmount())
                .maxAmount(scholarship.getMaxAmount())
                .deadline(scholarship.getDeadline() != null ? scholarship.getDeadline().toString() : null)
                .eligibility(scholarship.getEligibility())
                .requiredDocuments(scholarship.getRequiredDocuments())
                .steps(scholarship.getSteps())
                .applyUrl(scholarship.getApplyUrl())
                .build();
    }
}
