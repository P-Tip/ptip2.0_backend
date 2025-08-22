package com.ptip.like.Service;

import com.ptip.auth.entity.User;
import com.ptip.common.exception.ResourceNotFoundException;
import com.ptip.like.Entity.Like;
import com.ptip.like.Repository.LikeRepository;
import com.ptip.like.domain.TargetType;
import com.ptip.program.entity.Program;
import com.ptip.program.entity.Scholarship;
import com.ptip.program.repository.ProgramRepository;
import com.ptip.program.repository.ScholarshipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final ScholarshipRepository scholarshipRepository;
    private final ProgramRepository programRepository;

    public LikeService(LikeRepository likeRepository, ScholarshipRepository scholarshipRepository, ProgramRepository programRepository) {
        this.likeRepository = likeRepository;
        this.scholarshipRepository = scholarshipRepository;
        this.programRepository = programRepository;
    }

    @Transactional
    public String toggleLike(User user, TargetType targetType, int targetId) {
        if (targetType == TargetType.장학금) {
            Scholarship scholarship = scholarshipRepository.findById(targetId).orElseThrow(() -> new ResourceNotFoundException("해당 id의 장학금 프로그램을 찾을 수 없습니다."));
            Optional<Like> like = likeRepository.findByUserAndTargetTypeAndTargetId(user, targetType, targetId);

            if (like.isPresent()) {
                likeRepository.delete(like.get());
                scholarship.decreasePopularity();
                return "성공적으로 좋아요를 삭제했습니다.";
            } else {
                Like newLike = new Like(user, targetType, targetId);
                likeRepository.save(newLike);
                scholarship.increasePopularity();
                return "성공적으로 좋아요를 추가했습니다.";
            }
        } else {
            Program program = programRepository.findById(targetId).orElseThrow((() -> new ResourceNotFoundException("해당 id의 교내외 프로그램을 찾을 수 없습니다.")));
            Optional<Like> like = likeRepository.findByUserAndTargetTypeAndTargetId(user, targetType, targetId);

            if (like.isPresent()) {
                likeRepository.delete(like.get());
                program.decreasePopularity();
                return "성공적으로 좋아요를 삭제했습니다.";
            } else {
                Like newLike = new Like(user, targetType, targetId);
                likeRepository.save(newLike);
                program.increasePopularity();
                return "성공적으로 좋아요를 추가했습니다.";
            }
        }
    }
}
