package com.object.haru.alarm;

import com.object.haru.apply.ApplyEntity;
import com.object.haru.recruit.RecruitEntity;
import com.object.haru.rreview.RreviewEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter //보류
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "alarm")
public class AlarmEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="alarmid")
    private Long alarmid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="aid")
    private ApplyEntity aid;  //지원서 작성 알림용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rid")
    private RecruitEntity rid;  //지원 확정 알림용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rrid")
    private RreviewEntity rrid;  //리뷰 작성 확인 알림용


    @JoinColumn(name="kakaoid")
    private Long kakaoid;




    @Column( name = "title") // 알림 제목
    private String title;

    @Column( name = "body", length = 100) // 알림 내용
    private String body;

    @Column(name = "confirm", columnDefinition = "int default 1")
    private int confirm;


    @CreationTimestamp
    @Column(name="alTime")
    private LocalDateTime alTime; //도착 시간

    @Builder
    public AlarmEntity(Long kakaoid, RecruitEntity rid, RreviewEntity rrid, ApplyEntity aid, String title, String body) {
        this.kakaoid = kakaoid;
        this.rid = rid;
        this.aid = aid;
        this.rrid = rrid;
        this.title = title;
        this.body = body;
        this.confirm = 1;
    }
}
