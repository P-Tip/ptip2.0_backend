package com.ptip.like.Entity;

import com.ptip.auth.entity.User;
import com.ptip.like.domain.TargetType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type")
    private TargetType targetType;

    @Column(name = "target_id")
    private int targetId;

    public Like(User user, TargetType targetType, int targetId) {
        this.user = user;
        this.targetType = targetType;
        this.targetId = targetId;
    }
}
