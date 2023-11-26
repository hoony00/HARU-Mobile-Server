package com.object.haru.hreview;

import javax.transaction.Transactional;

import com.object.haru.alarm.AlarmService;
import com.object.haru.fcm.FirebaseCloudMessageService;
import com.object.haru.hreview.dto.HreviewDTO;
import com.object.haru.hreview.dto.HreviewResponseDTO;
import com.object.haru.recruit.RecruitRepository;
import com.object.haru.user.UserEntity;
import com.object.haru.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *    구직리뷰 관련 기능을 담당하는 Service
 *
 *   @version          1.00    2023.02.07
 *   @author           이상훈
 */

@Service
@RequiredArgsConstructor // final + not null 생성자 생성 -> 의존성 주입
@Transactional
public class HreviewService {

    private final HreviewRepository hreviewRepository;
    private final RecruitRepository recruitRepository;

    private final UserRepository userRepository;

    private final AlarmService alarmService;

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    public void SaveHreview(HreviewDTO hreviewDTO){  // 지원서 작성
        Long writerKakoid = hreviewDTO.getHwriterKakaoid();
        Long receiverKakoid = hreviewDTO.getHreceiverKakaoid();
        UserEntity writer = userRepository.findByKakaoid(writerKakoid);
        UserEntity receiver = userRepository.findByKakaoid(receiverKakoid);
        Long hwriter = writer.getUid();
        Long hreceiver = receiver.getUid();

        HreviewEntity hreview = HreviewEntity.builder()
                .hcontents(hreviewDTO.getHcontents())
                .hrating(hreviewDTO.getHrating())
                .rid(recruitRepository.getReferenceById(hreviewDTO.getRid()))
                .hreceiver(userRepository.findByKakaoid(hreviewDTO.getHreceiverKakaoid()))
                .hwriter(userRepository.findByKakaoid(hreviewDTO.getHwriterKakaoid()))
                .build();
      HreviewEntity review =  hreviewRepository.save(hreview);

      String title = "새로운 리뷰가 작성되었습니다!";

    /*    firebaseCloudMessageService.sendNotificationToUser(hreview.getHreceiver().getUid(),
                title, hreviewDTO.getHcontents(),"newReview");*/

        alarmService.saveAlarm(title, hreviewDTO.getHcontents(), review.getHreceiver().getUid(), review.getHrid(),"hrid");

    }



    public HreviewResponseDTO getHreviewById(Long hrid) {
        Optional<HreviewEntity> hreview = hreviewRepository.findById(hrid);
        if(hreview.isPresent()){
            Optional<UserEntity> userReceiver = userRepository.findById(hreview.get().getHreceiver().getUid());
            Optional<UserEntity> userWriter = userRepository.findById(hreview.get().getHwriter().getUid());
            double hrating = hreview.get().getHrating();

            return new HreviewResponseDTO(hreview, userReceiver.get().getUid(), userWriter.get().getUid(), hrating, hreview.get().getHcontents() );
        } else {
            throw new RuntimeException("Could not find Rreview with hrid: " + hrid);
        }
    }


    public List<HreviewEntity> countHrid(Long receiverKakaoid, Long writerKakaoid){
        return hreviewRepository.findByHreceiverKakaoidAndHwriterKakaoid(receiverKakaoid, writerKakaoid);
    }

    public List<HreviewEntity> careerHrid(Long kakaoid){
        return hreviewRepository.findByHreceiverKakaoid(kakaoid);
    }

    public Double calculateAverageRating(Long receiver){
        return hreviewRepository.findBySumRrating(receiver);
    }

    public Double avgHrid(Long kakaoid){
        List<HreviewEntity> kakao = hreviewRepository.findByHreceiverKakaoid(kakaoid);
        double totalRating = 0.0;
        int i = 0;
        for (HreviewEntity hreview : kakao) {
            totalRating += hreview.getHrating();
            i++;
        }
        Double avg = totalRating/i;
        return  Math.round(avg*10)/10.0; // 소수점 둘쨰에서 반올림 하여 첫째자리 까지 출력 ex) 3.7
    }



}
