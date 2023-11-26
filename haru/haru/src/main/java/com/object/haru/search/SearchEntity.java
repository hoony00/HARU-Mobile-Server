package com.object.haru.search;

import com.object.haru.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter //보류
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "search")
public class SearchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sid")
    private Long sid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="uid")
    private UserEntity uid;

    @Column( name = "keyword", length = 100) //키워드
    private String keyword;

    @Column( name = "open") //오픈
    private int open;

}