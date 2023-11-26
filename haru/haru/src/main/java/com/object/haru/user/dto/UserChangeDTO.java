package com.object.haru.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *    유저 정보 변경을 위한 RequestDTO
 *
 *   @version          1.00    2023.02.08
 *   @author           한승완
 */

@Data
@AllArgsConstructor
public class UserChangeDTO {

    private Long kakaoid;
    private String name;
    private String sex;
    private String age;
    private String career;
    private String photo;


}
