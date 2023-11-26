package com.object.haru.apply.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseRidDTO {
    private String myself;
    private String aage;
    private String acareer;
    private String asex;
    private LocalDateTime atime;
    private Long aid;
    private Long kakaoid;
    private Long rid;
    private Double avgRating;
    private String userName;

    public ResponseRidDTO(String userName, String myself, String aage, String acareer, String asex, LocalDateTime atime,Long aid, Long kakaoid, Long rid, Double avgRating) {
        this.userName = userName;
        this.myself = myself;
        this.aage = aage;
        this.acareer = acareer;
        this.asex = asex;
        this.atime = atime;
        this.aid = aid;
        this.kakaoid = kakaoid;
        this.rid = rid;
        this.avgRating = avgRating;
    }
}
