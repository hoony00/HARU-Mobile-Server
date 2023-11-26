package com.object.haru.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.object.haru.apply.ApplyEntity;

import com.object.haru.hreview.HreviewEntity;
import com.object.haru.recruit.RecruitEntity;
import com.object.haru.rreview.RreviewEntity;
import com.object.haru.search.SearchEntity;
import com.object.haru.zzim.ZzimEntity;
import lombok.*;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

/**
 *    user DB 엔티티
 *
 *   @version          1.00    2023.02.04
 *   @author           한승완
 */

@Getter
@Setter //보류
@Entity
@ToString
@Table(name = "user")
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="uid")
    private Long uid;

    @Column( name = "kakaoid", length = 100,nullable = false)
    private Long kakaoid; //카카오 고유 번호

    @Column( name = "password")
    private String password; //카카오 고유 번호로 생성한 비밀번호 -> 시큐리티를 위해 사용

    @Column( name = "name", length = 100) //이름
    private String name;

    @Column( name = "sex", length = 100) //성별
    private String sex;

    @Column( name = "age", length = 100) //나이
    private String age;

    @Column( name = "career", length = 200) //본인 경력 한줄 소개
    private String career;

    @Column( name = "photo", length = 200) //사진
    private String photo;

    @Column( name ="userrole")
    private String userrole; //유저 타입 -> 추후 기능 추가

    @JsonIgnore
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "uid")
    private final List<RecruitEntity> recruits = new ArrayList<>(); //구인 리스트

    @JsonIgnore
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "rreceiver")
    @Column(insertable = false, updatable = false)
    private final List<RreviewEntity> rreceivers = new ArrayList<>(); // 구인 리뷰 수신자 리스트

    @JsonIgnore
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "rwriter")
    @Column(insertable = false, updatable = false)
    private final List<RreviewEntity> rwriters = new ArrayList<>(); //구인 리뷰 작성자 리스트

    @JsonIgnore
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "hreceiver")
    @Column(insertable = false, updatable = false)
    private final List<HreviewEntity> hreceivers = new ArrayList<>(); //구직 리뷰 수신자 리스트

    @JsonIgnore
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "hwriter")
    @Column(insertable = false, updatable = false)
    private final List<HreviewEntity> hwriters = new ArrayList<>(); //구직 리뷰 작성자 리스트

    @JsonIgnore
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "uid")
    private final List<SearchEntity> searchs = new ArrayList<>(); //검색 기록 리스트

    @JsonIgnore
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "uid")
    private final List<ZzimEntity> zzims = new ArrayList<>(); //좋아요 리스트


    @JsonIgnore
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "uid")
    private final List<ApplyEntity> applys = new ArrayList<>(); //지원 리스트

    @Builder
    public UserEntity(Long uid,Long kakaoid, String password,String name,String sex,String age,String photo,String career,String userrole){
        this.uid = uid;
        this.kakaoid = kakaoid;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.photo = photo;
        this.career = career;
        this.userrole = userrole;
    }

}
