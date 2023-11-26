package com.object.haru.apply;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.object.haru.alarm.AlarmEntity;
import com.object.haru.recruit.RecruitEntity;
import com.object.haru.user.UserEntity;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *    apply DB 엔티티
 *
 *   @version          1.00    2023.02.07
 *   @author           이상훈
 */

@Getter
@Entity
@Setter
//@ToString(exclude = {"alarms"})
@NoArgsConstructor
@Table(name = "apply")
public class ApplyEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="aid")
    private Long aid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="uid")
    private UserEntity uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rid")
    private RecruitEntity rid;

    @Column( name = "aage", length = 100) //나이
    private String aage;

    @Column( name = "acareer", length = 100) //커리어
    private String acareer;

    @Column( name = "asex", length = 100) //성별
    private String asex;

    @Column( name = "myself", length = 100) //자기소개
    private String myself;

    @CreationTimestamp // INSERT시 자동으로 시간 추가
    @Column(name="aTime")
    private LocalDateTime aTime; //작성시간

    @JsonIgnore
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "aid", cascade = CascadeType.ALL)
    private List<AlarmEntity> alarms = new ArrayList<>();



    @Builder
    public ApplyEntity(@NonNull UserEntity uid, @NonNull RecruitEntity rid, String aage, String acareer, String asex, String myself) {
        this.uid = uid;
        this.rid = rid;
        this.aage = aage;
        this.acareer = acareer;
        this.asex = asex;
        this.myself = myself;
    }


}
