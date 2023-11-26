package com.object.haru.fcm;

import com.object.haru.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmTokenEntity, Long> {
    Optional<FcmTokenEntity> findByUid_Uid(@Param("uid") Long uid);
    Optional<FcmTokenEntity> findByUidUid(@Param("uid") UserEntity uid);
    Optional<FcmTokenEntity> findByUid_Kakaoid(@Param("kakaoid") long kakaoid);
}
