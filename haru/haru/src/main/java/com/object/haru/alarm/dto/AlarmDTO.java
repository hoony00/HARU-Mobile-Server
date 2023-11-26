package com.object.haru.alarm.dto;

import com.object.haru.apply.ApplyEntity;
import com.object.haru.fcm.FcmMessage;
import com.object.haru.recruit.RecruitEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDTO {
    private Long kakaoid;
    private  Long alarmid;
    private String body;
    private String title;
    private int confirm;
    private Long aid;
    private Long rid;
    private Long rrid;
    private LocalDateTime alTime;

}
