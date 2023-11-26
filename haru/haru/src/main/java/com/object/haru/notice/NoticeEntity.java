package com.object.haru.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import lombok.NoArgsConstructor;

@Getter
@Setter // 보류
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notice")
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="nid")
    private Long nid;

    @Column(name = "ncontents",length = 100)
    private String ncontent;

}
