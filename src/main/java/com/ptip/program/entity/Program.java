package com.ptip.program.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "programs")
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String category;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "application_start", nullable = false)
    private LocalDate applicationStart;

    @Column(name = "application_end", nullable = false)
    private LocalDate applicationEnd;

    @Column(name = "program_start", nullable = false)
    private LocalDate programStart;

    @Column(name = "program_end", nullable = false)
    private LocalDate programEnd;

    private String mode;

    private String location;

    private String tags;

    @Column(name = "how_to_apply", nullable = false)
    private String howToApply;

    @Column(name = "apply_url", nullable = false)
    private String applyUrl;

    private int popularity;
}
