package com.ptip.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String kakaoId;

    @Column(nullable = false)
    private String nickname;

    private String img;

    @Column(nullable = false)
    private String email;

    private String refreshToken;

    @Column
    private String schoolEmail;

    @Column(nullable = false)
    private boolean schoolEmailVerified = false;

    @Column
    private String schoolEmailVerificationCode;
}