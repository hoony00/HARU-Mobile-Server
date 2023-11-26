package com.object.haru.apply.dto;

import com.object.haru.apply.ApplyEntity;
import com.object.haru.user.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
public class DetailResponseDTO {
    private String name; // 유저 아이디
    private String aage; // 나이
    private String acareer; // 경력
    private Long kakaoid; // kakaoid
    private Long rid; // Rid
    private Double avgRating; // rating
    private String asex; // 성별
    private String myself; // 자기소개
    private LocalDateTime aTime; // 작성시간

    /*private String rating; // 평점*/

    public DetailResponseDTO(Optional<ApplyEntity> applyEntity, Optional<UserEntity> userEntity, Long kakaoid, Double avgRating,Long rid){
        name = userEntity.get().getName();
        aage = applyEntity.get().getAage();
        acareer = applyEntity.get().getAcareer();
        asex = applyEntity.get().getAsex();
        myself = applyEntity.get().getMyself();
        aTime = applyEntity.get().getATime();
        this.kakaoid = kakaoid;
        this.avgRating = avgRating;
        this.rid = rid;
    }
}
