package com.object.haru.rreview;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;


import com.object.haru.alarm.AlarmService;
import com.object.haru.fcm.FirebaseCloudMessageService;
import com.object.haru.recruit.RecruitEntity;
import com.object.haru.recruit.RecruitRepository;
import com.object.haru.rreview.dto.RreviewDTO;
import com.object.haru.rreview.dto.RreviewResponseDTO;
import com.object.haru.user.UserEntity;
import com.object.haru.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 *    구인 리뷰 관련 기능을 담당하는 Service
 *
 *   @version          1.00    2023.02.07
 *   @author           이상훈
 */

@Service
@RequiredArgsConstructor
@Transactional
public class RreviewService {

    private final RreviewRepository rreviewRepository;
    private final RecruitRepository recruitRepository;
    private final UserRepository userRepository;

    private final AlarmService alarmService;

    @Autowired
    private FirebaseCloudMessageService firebaseCloudMessageService;
    public void SaveRreview(RreviewDTO rreviewDTO){  // 지원서 작성
        Long writerKakoid = rreviewDTO.getRwriterKakaoid();
        Long receiverKakoid = rreviewDTO.getRreceiverKakaoid();
        UserEntity writer = userRepository.findByKakaoid(writerKakoid);
        UserEntity receiver = userRepository.findByKakaoid(receiverKakoid);
        Long hwriter = writer.getUid();
        Long hreceiver = receiver.getUid();

        RreviewEntity rreview = RreviewEntity.builder()
                .rcontents(rreviewDTO.getRcontents())
                .rrating(rreviewDTO.getRrating())
                .rid(recruitRepository.getReferenceById(rreviewDTO.getRid()))
                .rreceiver(userRepository.findByKakaoid(rreviewDTO.getRreceiverKakaoid()))
                .rwriter(userRepository.findByKakaoid(rreviewDTO.getRwriterKakaoid()))
                .build();
        RreviewEntity review = rreviewRepository.save(rreview);
        String title = "새로운 리뷰가 작성되었습니다!";
        firebaseCloudMessageService.sendNotificationToUser(review.getRid().getUid().getKakaoid(),
                title, rreviewDTO.getRcontents(), "newReview" ,review.getRrid().toString());

        alarmService.saveAlarm(title, rreviewDTO.getRcontents(), rreviewDTO.getRreceiverKakaoid(), review.getRrid(),"rrid");
    }

    public RreviewResponseDTO getRreviewById(Long rrid) {
        Optional<RreviewEntity> rreview = rreviewRepository.findById(rrid);
        if(rreview.isPresent()){
            Optional<UserEntity> userReceiver = userRepository.findById(rreview.get().getRreceiver().getUid());
            Optional<UserEntity> userWriter = userRepository.findById(rreview.get().getRwriter().getUid());
            double rrating = rreview.get().getRrating();

            return new RreviewResponseDTO(rreview, userReceiver.get().getUid(), userWriter.get().getUid(), rrating, rreview.get().getRcontents() );
        } else {
            throw new RuntimeException("Could not find Rreview with id: " + rrid);
        }
    }

    public List<RreviewEntity> checkRrid(Long kakaoRreceiver, Long kakaoRwriter){
        return rreviewRepository.findByRreceiverKakaoidAndRwriterKakaoid(kakaoRreceiver,kakaoRwriter);
    }

    public List<RreviewEntity> careerRrid(Long kakaoRreceiver){
        return rreviewRepository.findByRreceiverKakaoid(kakaoRreceiver);
    }

    public Double avgHrid(Long kakaoid){
        List<RreviewEntity> kakao = rreviewRepository.findByRreceiverKakaoid(kakaoid);
        double totalRating = 0.0;
        int i = 0;
        for (RreviewEntity hreview : kakao) {
            totalRating += hreview.getRrating();
            i++;
        }
        Double avg = totalRating/i;
        return  Math.round(avg*10)/10.0; // 소수점 둘쨰에서 반올림 하여 첫째자리 까지 출력 ex) 3.7
    }

    //getReviewsByRecruit 메서드는 Recruit의 id를 입력받아 해당 모집 공고에 대한 모든 리뷰를 조회합니다.
    public List<RreviewEntity> getReviewsByRecruit(Long rid) {
        RecruitEntity recruit = recruitRepository.findById(rid).orElseThrow(() -> new EntityNotFoundException("Recruit not found"));
        return rreviewRepository.findByRid(recruit.getRid());
    }


}
