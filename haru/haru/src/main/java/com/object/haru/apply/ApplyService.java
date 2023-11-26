package com.object.haru.apply;

import com.object.haru.alarm.AlarmService;
import com.object.haru.apply.dto.ApplyRequestDTO;
import com.object.haru.apply.dto.DetailResponseDTO;
import com.object.haru.apply.dto.ResponseRidDTO;
import com.object.haru.fcm.FirebaseCloudMessageService;
import com.object.haru.recruit.RecruitEntity;
import com.object.haru.recruit.RecruitRepository;
import com.object.haru.recruit.dto.ResponseDTO;
import com.object.haru.rreview.RreviewService;
import com.object.haru.user.UserEntity;
import com.object.haru.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *    지원서 관련 기능을 담당하는 Service
 *
 *   @version          1.00    2023.02.12
 *   @author           이상훈
 */

@Service
@RequiredArgsConstructor // final + not null 생성자 생성 -> 의존성 주입
@Transactional
public class ApplyService {

    private final ApplyRepository applyRepository;
    private final RecruitRepository recruitRepository;
    private final UserRepository userRepository;
    private final AlarmService alarmService;
    private final RreviewService rreviewService;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    public void SaveApply(ApplyRequestDTO applyRequestDTO) {  // 지원서 작성
        ApplyEntity apply = ApplyEntity.builder().
                myself(applyRequestDTO.getMyself()).
                acareer(applyRequestDTO.getAcareer()).
                asex(applyRequestDTO.getAsex()).
                aage(applyRequestDTO.getAage()).
                rid(recruitRepository.getReferenceById(applyRequestDTO.getRid())).
                uid(userRepository.findByKakaoid(applyRequestDTO.getKakaoid())).build();
        ApplyEntity saveApply = applyRepository.save(apply);

        String title = "새로운 지원서가 도착했습니다!";
        firebaseCloudMessageService.sendNotificationToUser(saveApply.getRid().getUid().getKakaoid(),
                title, applyRequestDTO.getMyself(), "newApply", saveApply.getAid().toString());

        System.out.println("uid :" + saveApply.getRid().getUid().getUid());
        Long uid = saveApply.getRid().getUid().getUid();
        alarmService.saveAlarm(title, applyRequestDTO.getMyself(), uid, saveApply.getAid(), "aid");
    }


    public DetailResponseDTO FindOneApply(Long aid) {
        Optional<ApplyEntity> apply = applyRepository.findById(aid);
        if (apply.isEmpty()) {
            // apply가 null인 경우 처리 로직 짜기
        }
        UserEntity user = apply.get().getUid();
        Optional<UserEntity> user_uid = userRepository.findById(user.getUid());
        Long kakaoid = user_uid.get().getKakaoid();
        Double avgRating = rreviewService.avgHrid(kakaoid);


        DetailResponseDTO applyDTO = new DetailResponseDTO(apply, user_uid, kakaoid, avgRating, apply.get().getRid().getRid());
        return applyDTO;
    }

    public Optional<ApplyEntity> countApply(Long rid, Long kakaoid) {
        return applyRepository.findByRidRidAndUidKakaoid(rid, kakaoid);
    }

    public ApplyEntity recentApply(Long rid, Long kakaoid) {
        return applyRepository.findByRid_RidAndUid_Kakaoid(rid, kakaoid);
    }

    public void deleteApply(Long aid) {
        applyRepository.deleteById(aid);
    }// 지원서 삭제


    public List<ResponseRidDTO> findByRidWithRating(Long rid) {
        return applyRepository.findByRidWithRating(rid);
    }

    public List<ResponseDTO> FindByKakaoid(Long kakaoid) { //유저로 지원서 SELECT
        UserEntity user = userRepository.findByKakaoid(kakaoid);
        List<ApplyEntity> applyEntities = applyRepository.findapplytest(user);

        return applyEntities.stream()
                .map(applyEntity -> {
                    RecruitEntity recruit = applyEntity.getRid();
                    boolean hasZzim = recruit.getZzims().stream()
                            .anyMatch(zzim -> zzim.getUid().equals(user));
                    return new ResponseDTO(recruit, hasZzim);
                })
                .collect(Collectors.toList());
    }

    public ResponseDTO getRecentApply(Long kakaoid) {
        UserEntity user = userRepository.findByKakaoid(kakaoid);
        ApplyEntity apply = applyRepository.findTopByUidOrderByAidDesc(user);

        if (apply == null) {
            return new ResponseDTO(null, false);
        }

        RecruitEntity recruit = apply.getRid();
        boolean hasZzim = recruit.getZzims().stream()
                .anyMatch(zzim -> zzim.getUid().equals(user));

        return new ResponseDTO(recruit, hasZzim);
    }




}
