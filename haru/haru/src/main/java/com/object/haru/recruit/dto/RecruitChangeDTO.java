package com.object.haru.recruit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecruitChangeDTO {

    private Long rid; //글 번호
    private Long kakaoid; // 유저 카카오 아이디
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
}
