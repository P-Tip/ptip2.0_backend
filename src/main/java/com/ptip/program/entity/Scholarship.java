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

    @Column(columnDefinition = "TEXT")
    private String description;

    private String department;

    @Column(name = "min_amount")
    private int minAmount;

    @Column(name = "max_amount")
    private int maxAmount;

    private LocalDate deadline;

    private String eligibility;

    @Column(name = "required_documents")
    private String requiredDocuments;

    private String steps;

    @Column(name = "apply_url")
    private String applyUrl;

    private int popularity;

    public void increasePopularity() {
        this.popularity += 1;
    }

    public void decreasePopularity() {
        this.popularity = Math.max(0, this.popularity - 1);
    }
}
