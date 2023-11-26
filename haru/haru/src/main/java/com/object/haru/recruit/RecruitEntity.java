package com.object.haru.recruit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.object.haru.alarm.AlarmEntity;
import com.object.haru.apply.ApplyEntity;
import com.object.haru.hreview.HreviewEntity;
import com.object.haru.rreview.RreviewEntity;
import com.object.haru.user.UserEntity;
import com.object.haru.zzim.ZzimEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import lombok.*;


import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.repository.EntityGraph;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *    recruit DB 엔티티
 *
 *   @version          1.00    2023.02.02
 *   @author           한승완
 */
@Getter
@Entity
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recruit")
public class RecruitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rid")
    private Long rid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name="uid",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT)) //
    private UserEntity uid; // 외래키

    @Column(name="title", length = 100)
    private String title; // 제목

    @Column(name="subject", length = 100)
    private String subject; // 분야
    @Column(name="stTime")
    private String stTime; //시작시간 2023-01-30 한승완 스트링 수정

    @Column(name="endTime")
    private String endTime; //끝나는 시간 2023-01-30 한승완 스트링 수정
    @Column(name="pay")
    private Integer pay; // 돈

    @Column(name="addr", length = 100)
    private String addr; // 주소

    @Column(name = "lat")
    private Double lat; //위도

    @Column(name = "lon")
    private Double lon; //경도

    @Column(name="rage", length = 100)
    private String rage; // 나이

    @Column(name="rsex", length = 100)
    private String rsex; // 성별

    @Column(name="rcareer", length = 100)
    private String rcareer; // 경력

    @Column(name="person", length = 100)
    private Long person; // 확정된 사람

    @Column(name="step", length = 100)
    private String step; // 진행단계

    @CreationTimestamp // INSERT시 자동으로 시간 추가
    @Column(name="rTime")
    private LocalDateTime rTime; //작성시간

    @Column(name="count")
    private Integer count; //조회수

    @JsonIgnore
    @OneToMany(mappedBy = "rid")
    @BatchSize(size = 100)
    private final List<ApplyEntity> applys = new ArrayList<>(); //지원 리스트 (한승완 2023-01-31)

    @JsonIgnore
    @OneToMany(mappedBy = "rid")
    @BatchSize(size = 100)
    private final List<RreviewEntity> rreviews = new ArrayList<>(); //구인 리뷰 리스트

    @JsonIgnore
    @OneToMany(mappedBy = "rid")
    @BatchSize(size = 100)
    private final List<HreviewEntity> hreviews = new ArrayList<>(); //구직 리뷰 리스트

    @JsonIgnore
    @OneToMany(mappedBy = "rid")
    @BatchSize(size = 100)
    private final List<ZzimEntity> zzims = new ArrayList<>(); //좋아요 리스트

    @JsonIgnore
    @OneToMany(mappedBy = "aid")
    @BatchSize(size = 100)
    private List<AlarmEntity> alarms = new ArrayList<>(); //알림 리스트

    @Builder
    public RecruitEntity(UserEntity uid, String title, String subject, String stTime,
                         String endTime, int pay, String addr,Double lat,Double lon, String rage, String rcareer, Long person,
                         String step, int count,String rsex ){
        this.uid = uid;
        this.title = title;
        this.subject = subject;
        this.stTime = stTime;
        this.endTime = endTime;
        this.pay = pay;
        this.addr = addr;
        this.lat = lat;
        this.lon = lon;
        this.rage = rage;
        this.rcareer = rcareer;
        this.person = person;
        this.step = step;
        this.count = count;
        this.rsex = rsex;
    }


}
