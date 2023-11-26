package com.object.haru.hreview.dto;

import com.object.haru.hreview.HreviewEntity;
import com.object.haru.user.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
public class HreviewResponseDTO {
    private long hwriter;
    private long hreceiver;
    private String hcontents;
    private LocalDateTime hrTime; // 작성시간
    private  double hrating;

    public HreviewResponseDTO(Optional<HreviewEntity> hreview, long userReceiver, long userWriter, double hrating, String hcontents){
        hreceiver = userReceiver;
        hwriter = userWriter;
        this.hrating = hrating;
        this.hcontents = hcontents;
        hrTime = hreview.get().getHrTime();
    }

}
