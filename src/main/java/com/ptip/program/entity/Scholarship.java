package com.ptip.program.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "scholarships")
public class Scholarship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    private String department;

    @Column(name = "min_amount", nullable = false)
    private int minAmount;

    @Column(name = "max_amount", nullable = false)
    private int maxAmount;

    private LocalDate deadline;

    private String eligibility;

    @Column(name = "required_documents", nullable = false)
    private String requiredDocuments;

    private String steps;

    @Column(name = "apply_url", nullable = false)
    private String applyUrl;

    private int popularity;
}
