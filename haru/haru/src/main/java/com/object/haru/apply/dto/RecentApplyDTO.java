package com.object.haru.apply.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentApplyDTO {

    private Long rid; // 유저 아이디

    private Long aid; // 글 번호

    private String step; // 나이

    private String title; // 경력


}