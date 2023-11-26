package com.object.haru.rreview;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 *    구인 리뷰 기능을 담당하는 Repository
 *
 *   @version          1.00    2023.02.07
 *   @author           이상훈
 */
public interface RreviewRepository extends JpaRepository<RreviewEntity, Long> {

    Optional<RreviewEntity> findById(Long rrid);

    List<RreviewEntity> findByRreceiverKakaoidAndRwriterKakaoid(Long receiverKakaoid, Long writerKakaoid);

    List<RreviewEntity> findByRreceiverKakaoid(Long Kakaoid);

    @Query(value = "SELECT sum(rrating) from rreview where rreceiver = ?1", nativeQuery = true)
    Double findBySumRrating(@Param(value = "rreceiver") Long rreceiver);

    List<RreviewEntity> findByRid(Long rid);
}






