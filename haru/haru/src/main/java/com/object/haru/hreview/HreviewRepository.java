package com.object.haru.hreview;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 *    구직 리뷰 기능을 담당하는 Repository
 *
 *   @version          1.00    2023.02.07
 *   @author           이상훈
 *
 */

public interface HreviewRepository extends JpaRepository<HreviewEntity, Long> {

    Optional<HreviewEntity> findById(Long hrid);

    List<HreviewEntity> findByHreceiverKakaoidAndHwriterKakaoid(Long receiverKakaoid, Long writerKakaoid);

    List<HreviewEntity> findByHreceiverKakaoid(Long Kakaoid);

    @Query(value = "SELECT sum(hrating) from hreview where hreceiver = ?1", nativeQuery = true)
    Double findBySumRrating(@Param(value = "hreceiver") Long hreceiver);



}
