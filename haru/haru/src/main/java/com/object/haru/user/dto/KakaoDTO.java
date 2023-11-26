package com.object.haru.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoDTO {

    private Long kakaoid; // 카카오 ID 번호
    private String name; // 사용자 이름
    private String photo; // 사용자 사진

}
