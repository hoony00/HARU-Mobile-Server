package com.object.haru.rreview;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.object.haru.alarm.AlarmEntity;
import com.object.haru.recruit.RecruitEntity;
import com.object.haru.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *    Rreview DB 엔티티
 *
 *   @version          1.00    2023.02.07
 *   @author           이상훈
 */

@Getter
@Setter //보류
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rreview", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"rid", "rwriter", "rreceiver"}),   //같은 데이터 중복 방지
        @UniqueConstraint(columnNames = {"rid", "rreceiver", "rwriter"})
})
public class RreviewEntity { //대타 알바생의 대한 리뷰

    @PrePersist // sql 실행시 먼저 자동실행 만약 보내는이와 받는이가 같다면 실행되지 않음
    public void prePersist() {
        if (this.rreceiver.getUid() == this.rwriter.getUid()) {
            throw new RuntimeException("rreceiver and rwriter cannot be the same");
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rrid")
    private Long rrid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name="rid", updatable = false)
    private RecruitEntity rid; // 외래키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name="rwriter")
    private UserEntity rwriter; // 작성자(사장님)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name="rreceiver")
    private UserEntity rreceiver; // 받는이(알바생)


    @Column(name="rcontents", length = 100)
    private String rcontents; // 내용

    @CreationTimestamp
    @Column(name="rrTime")
    private LocalDateTime rrTime; //작성시간

    @Column(name = "rrating", length = 3)
    @ColumnDefault("0")
    private double rrating; // 알바생 평점

    @JsonIgnore
    @OneToMany(mappedBy = "rrid")
    private List<AlarmEntity> alarms = new ArrayList<>(); //알림 리스트

    @Builder
    public RreviewEntity(RecruitEntity rid, UserEntity rwriter, UserEntity rreceiver,  String rcontents, double rrating){
        this.rid = rid;
        this.rreceiver = rreceiver;
        this.rwriter = rwriter;
        this.rcontents = rcontents;
        this.rrating = rrating;

    }

}
