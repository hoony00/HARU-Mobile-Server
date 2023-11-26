package com.object.haru.recruit.dto;

import com.object.haru.recruit.RecruitEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResponseDTO{ // 특정 API 응답형태를위한 내부 DTO (2023-01-31 한승완)

    private Long rid;
    private String title; // 제목
    private String subject; // 분야
    private String stTime;
    private String endTime; // 끝나는 시간
    private int pay; // 돈
    private String addr; // 주소 = 위도+경도라고 생각됨
    private String step;
    private int count;
    private boolean zzim;
    private LocalDateTime rTime;
    private Long kakaoid;

    public ResponseDTO(RecruitEntity recruit, boolean zzim) {
        if (recruit != null) {
            this.rid = recruit.getRid();
            this.title = recruit.getTitle();
            this.subject = recruit.getSubject();
            this.stTime = recruit.getStTime();
            this.endTime = recruit.getEndTime();
            this.pay = recruit.getPay();
            this.addr = recruit.getAddr();
            this.step = recruit.getStep();
            this.count = recruit.getCount();
            this.rTime = recruit.getRTime();
            this.kakaoid = recruit.getUid().getKakaoid();
        } else {
            this.rid = null;
            this.title = null;
            this.subject = null;
            this.stTime = null;
            this.endTime = null;
            this.pay = 0;
            this.addr = null;
            this.step = null;
            this.count = 0;
            this.rTime = null;
            this.kakaoid = null;
        }
        this.zzim = zzim;
    }

}