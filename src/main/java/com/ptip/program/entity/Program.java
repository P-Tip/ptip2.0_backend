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

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "application_start")
    private LocalDate applicationStart;

    @Column(name = "application_end")
    private LocalDate applicationEnd;

    @Column(name = "program_start")
    private LocalDate programStart;

    @Column(name = "program_end")
    private LocalDate programEnd;

    private String mode;

    private String location;

    private String tags;

    @Column(name = "how_to_apply")
    private String howToApply;

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
