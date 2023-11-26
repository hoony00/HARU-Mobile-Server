package com.object.haru.zzim;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *    찜 관련 기능을 담당하는 Repository
 *
 *   @version          1.00    2023.02.07
 *   @author           이상훈
 */

public interface ZzimRepository extends JpaRepository<ZzimEntity, Long> {
    List<ZzimEntity> findByUidKakaoid(Long kakaoid);
    void deleteByRidRidAndUidKakaoid(Long rid, Long kakaoid);
    List<ZzimEntity> findByRidRidAndUidKakaoid(Long rid, Long kakaoid);
}
