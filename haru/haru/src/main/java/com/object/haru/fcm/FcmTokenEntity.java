package com.object.haru.fcm;

import com.object.haru.user.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "fcmToken")
@Entity

public class FcmTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="fid")
    private Long fid;

    @Column( name = "fcmToken", length = 900) //커리어
    private String fcmToken;

    @UpdateTimestamp
    @Column( name = "fTime", length = 100) //커리어
    private LocalDateTime fTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private UserEntity uid;

    public void updateToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    @Builder
    public FcmTokenEntity(String fcmToken, UserEntity uid) {
        this.fcmToken = fcmToken;
        this.uid = uid;
    }
}

