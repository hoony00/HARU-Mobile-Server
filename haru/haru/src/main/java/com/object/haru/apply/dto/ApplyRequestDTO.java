package com.object.haru.apply.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplyRequestDTO {

    private Long kakaoid; // 유저 아이디

    private Long rid; // 글 번호

    private String aage; // 나이

    private String acareer; // 경력

    private String asex; // 성별

    private String myself; // 자기소개

}