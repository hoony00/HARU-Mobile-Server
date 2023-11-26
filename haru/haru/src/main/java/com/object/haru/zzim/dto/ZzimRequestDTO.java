package com.object.haru.zzim.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ZzimRequestDTO {
    private long rid; // 글 번호
    private long Kakaoid; // 유저 번호
}
