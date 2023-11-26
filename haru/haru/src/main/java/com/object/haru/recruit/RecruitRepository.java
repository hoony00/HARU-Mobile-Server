package com.object.haru.recruit;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *    구인관련 기능을 담당하는 Repository
 *
 *   @version          1.00    2023.02.01
 *   @author           한승완
 */

import java.util.List;
import java.util.Optional;

public interface
RecruitRepository extends JpaRepository<RecruitEntity,Long> {

//    @Query(value = "SELECT *,(6371*ACOS(COS(RADIANS(recruit.lat))*COS(RADIANS(?1))*COS(RADIANS(?2)-RADIANS(recruit.lon))" +
//            "+SIN(RADIANS(recruit.lat))*SIN(RADIANS(?1)))) AS d FROM recruit  WHERE step = '모집중' HAVING d < ?3 ORDER BY rid DESC LIMIT ?4, 20",nativeQuery = true)
//    List<RecruitEntity> FindAllRecruit(@Param(value="lat") Double lat,
//                                       @Param(value = "lon") Double lon,
//                                       @Param(value = "distance") Double distance,
//                                       @Param(value = "page") int page);

    @Query("SELECT r FROM RecruitEntity r LEFT JOIN FETCH r.zzims zzim WHERE r.step = '모집중' AND (FUNCTION('ABS', r.lat + r.lon - :sum) <= 0.3) ORDER BY r.rid DESC")
    List<RecruitEntity> findAllRecruitWithZzims(@Param("sum") Double sum, Pageable pageable);


    @Query("SELECT r FROM RecruitEntity r LEFT JOIN FETCH r.zzims z WHERE (r.title LIKE %:search% OR r.subject LIKE %:search% OR r.addr LIKE %:search% OR r.rcareer LIKE %:search%) AND r.step = '모집중' ORDER BY r.rid DESC")
    List<RecruitEntity> findBySearchWithZzims(@Param("search") String search);




    List<RecruitEntity> findByUidKakaoidOrderByRidAsc(Long kakaoid);


    Optional<RecruitEntity> findTopByUidKakaoidOrderByRidDesc(Long kakaoid);


}
