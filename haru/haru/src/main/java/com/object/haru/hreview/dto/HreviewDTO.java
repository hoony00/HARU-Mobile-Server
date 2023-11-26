package com.object.haru.hreview.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HreviewDTO {
    private long rid; //recruit 외래키
    private long hwriterKakaoid;
    private long hreceiverKakaoid;
    private String hcontents;
    private  Double hrating;

}