package com.object.haru.zzim;


import com.object.haru.recruit.RecruitEntity;
import com.object.haru.user.UserEntity;
import lombok.*;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *    zzim(찜) DB 엔티티
 *
 *   @version          1.00    2023.02.07
 *   @author           이상훈
 */

@Getter
@Entity
@IdClass(RidUid.class)
@ToString
@Table(name = "zzim")
@NoArgsConstructor
public class ZzimEntity {
    @Id
    @ManyToOne
    @JoinColumn(name="uid")
    private UserEntity uid;
    @Id
    @ManyToOne
    @JoinColumn(name="rid")
    private RecruitEntity rid;

    @Builder
    public ZzimEntity(UserEntity uid, RecruitEntity rid){
        this.uid = uid;
        this.rid = rid;
    }

}
