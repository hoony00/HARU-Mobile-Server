package com.object.haru.rreview.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RreviewDTO {

    private long rid; //외래키
    private long rwriterKakaoid;
    private long rreceiverKakaoid;
    private String rcontents;
    private  double rrating;


}