package com.object.haru.recruit.dto;

import com.object.haru.recruit.RecruitEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
public class SpecificResponseDTO {

    private Long rid; //rid

    private String title; // 제목

    private String subject; // 분야

    private String stTime; // 시작시간

    private String endTime; // 끝나는 시간

    private int pay; // 돈

    private String addr; // 주소

    private Double lat; // 위도

    private Double lon; //경도

    private String rage; // 나이

    private String rsex; // 성별

    private String rcareer; // 경력

    private Long person; // 확정된사람

    private String step; // 단계

    private int count; // 조회수

    private String name; // 유저 아이디

    private LocalDateTime Rtime;

    private Long kakaoid;

    public SpecificResponseDTO(Optional<RecruitEntity> recruitEntity){
        rid = recruitEntity.get().getRid();
        title = recruitEntity.get().getTitle();
        subject = recruitEntity.get().getSubject();
        endTime = recruitEntity.get().getEndTime();
        pay = recruitEntity.get().getPay();
        addr = recruitEntity.get().getAddr();
        rage = recruitEntity.get().getRage();
        rcareer = recruitEntity.get().getRcareer();
        person = recruitEntity.get().getPerson();
        rsex = recruitEntity.get().getRsex();
        step = recruitEntity.get().getStep();
        lat = recruitEntity.get().getLat();
        lon = recruitEntity.get().getLon();
        count = recruitEntity.get().getCount();
        name = recruitEntity.get().getUid().getName();
        stTime = recruitEntity.get().getStTime();
        Rtime = recruitEntity.get().getRTime();
        kakaoid = recruitEntity.get().getUid().getKakaoid();
    }



}
