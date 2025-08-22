package com.ptip.like.Repository;

import com.ptip.auth.entity.User;
import com.ptip.like.Entity.Like;
import com.ptip.like.domain.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByUserAndTargetTypeAndTargetId(User user, TargetType targetType, int targetId);
    List<Like> findAllByUserIdAndTargetTypeAndTargetIdIn(int userId, TargetType targetType, List<Integer> programIds);
}
