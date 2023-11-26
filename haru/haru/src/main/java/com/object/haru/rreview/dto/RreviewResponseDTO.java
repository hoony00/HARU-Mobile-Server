package com.object.haru.rreview.dto;

import com.object.haru.hreview.HreviewEntity;
import com.object.haru.recruit.RecruitEntity;
import com.object.haru.rreview.RreviewEntity;
import com.object.haru.user.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
public class RreviewResponseDTO {
    private long rwriter;
    private long rreceiver;
    private String rcontents; // 내용
    private LocalDateTime rrTime; // 작성시간
    private double rrating;


    public RreviewResponseDTO(Optional<RreviewEntity> rreview, long userReceiver, long userWriter, double rrating, String rcontents){
        rreceiver = userReceiver;
        rwriter = userWriter;
        this.rrating = rrating;
        this.rcontents = rcontents;
        rrTime = rreview.get().getRrTime();
    }

}
