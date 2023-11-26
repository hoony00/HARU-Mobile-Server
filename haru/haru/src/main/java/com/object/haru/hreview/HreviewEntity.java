package com.object.haru.hreview;

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

/**
 *    Hreview DB 엔티티
 *
 *   @version          1.00    2023.02.22
 *   @author           이상훈
 */

@Getter
@Setter //보류
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hreview", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"rid", "hwriter", "hreceiver"}),
        @UniqueConstraint(columnNames = {"rid", "hreceiver", "hwriter"})
})
public class HreviewEntity { //사장님(가게)의 대한 리뷰

    @PrePersist // sql 실행시 먼저 자동실행 만약 보내는이와 받는이가 같다면 실행되지 않음
    public void prePersist() {
        if (this.hreceiver.getUid() == this.hwriter.getUid()) {
            throw new RuntimeException("hreceiver and hwriter cannot be the same");
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hrid")
    private Long hrid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rid",updatable = false)
    private RecruitEntity rid; // 외래키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name="hwriter")
    private UserEntity hwriter; // 작성자(구인)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name="hreceiver")
    private UserEntity hreceiver; // 작성자(구인)

    @Column(name = "hcontents", length = 100)
    private String hcontents; // 내용

    @CreationTimestamp
    @Column(name="hrTime")
    private LocalDateTime hrTime; //작성시간

    @Column(name = "hrating", length = 3)
    @ColumnDefault("0")
    private double hrating; // 알바생 평점


    @Builder
    public HreviewEntity(RecruitEntity rid, UserEntity hwriter, UserEntity hreceiver,  String hcontents, double hrating){
        this.rid = rid;
        this.hreceiver = hreceiver;
        this.hwriter = hwriter;
        this.hcontents = hcontents;
        this.hrating = hrating;

    }

}

