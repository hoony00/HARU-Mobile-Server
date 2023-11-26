package com.object.haru.user;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 *    유저 관련 기능을 담당하는 Repository
 *
 *   @version          1.00    2023.02.01
 *   @author           한승완
 */

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.kakaoid=?1")
    Optional<UserEntity> FindByKakaoId(Long kakaoid);

    UserEntity findByKakaoid(@Param(value = "kakaoid")Long kakaoid);

    @Query("SELECT u.kakaoid FROM UserEntity u WHERE u.uid = :uid")
    Long findKakaoidByUid(@Param("uid") Long uid);

}
