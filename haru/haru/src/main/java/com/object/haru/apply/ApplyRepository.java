package com.object.haru.apply;
import com.object.haru.apply.dto.ResponseRidDTO;
import com.object.haru.user.UserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 *    지원서관련 기능을 담당하는 Repository
 *
 *   @version          1.00    2023.02.07
 *   @author           이상훈
 */

public interface ApplyRepository extends JpaRepository<ApplyEntity,Long> {

    List<ApplyEntity> findByRid_Rid(Long rid);

    @Query("SELECT a FROM ApplyEntity a JOIN FETCH a.rid r WHERE a.uid = :user ORDER BY a.aid ASC")
    List<ApplyEntity> findapplytest(@Param("user") UserEntity user);

    Optional<ApplyEntity> findById(Long id);

    @Query("SELECT new com.object.haru.apply.dto.ResponseRidDTO(a.uid.name, a.myself,a.aage,a.acareer,a.asex,a.aTime,a.aid, a.uid.kakaoid, a.rid.rid, AVG(r.rrating)) " +
            "FROM ApplyEntity a " +
            "LEFT JOIN RreviewEntity r ON a.uid = r.rreceiver " +
            "WHERE a.rid.rid = :rid " +
            "GROUP BY a.uid, a.myself, a.aid, a.uid.uid, a.rid.rid")
    List<ResponseRidDTO> findByRidWithRating(@Param("rid") Long rid);

    ApplyEntity findTopByUidOrderByAidDesc(@Param("user") UserEntity user);
    Optional<ApplyEntity> findByRidRidAndUidKakaoid(Long rid, Long kakaoid);

    ApplyEntity findByRid_RidAndUid_Kakaoid(Long rid, Long kakaoid);



}




