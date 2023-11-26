package com.object.haru.alarm;

import javax.transaction.Transactional;

import com.object.haru.apply.ApplyEntity;
import com.object.haru.apply.ApplyRepository;

import com.object.haru.recruit.RecruitEntity;
import com.object.haru.recruit.RecruitRepository;
import com.object.haru.rreview.RreviewEntity;
import com.object.haru.rreview.RreviewRepository;
import com.object.haru.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor // final + not null 생성자 생성 -> 의존성 주입
@Transactional
public class AlarmService {


    private final AlarmRepository alarmRepository;
    private final RecruitRepository recruitRepository;
    private final UserRepository userRepository;
    private final RreviewRepository rreviewRepository;
    private final ApplyRepository applyRepository;


    public void saveAlarm(String title, String body, Long uid, Long id, String obj) {

        Long kakaoid  = userRepository.findKakaoidByUid(uid);
        if(kakaoid == null){
            kakaoid = uid;
        }
        AlarmEntity alarmEntity = AlarmEntity.builder()
                .title(title)
                .body(body)
                .kakaoid(kakaoid)
                .build();

        System.out.println("알람 kakaoid : "+ kakaoid);


        if (obj.equals("aid")) {
            ApplyEntity applyEntity = applyRepository.getReferenceById(id);
            alarmEntity.setAid(applyEntity);
        } else if (obj.equals("rid")) {
            RecruitEntity recruitEntity = recruitRepository.getReferenceById(id);
            alarmEntity.setRid(recruitEntity);
        }  else if (obj.equals("rrid")) {
            RreviewEntity rreviewEntity = rreviewRepository.getReferenceById(id);
            alarmEntity.setRrid(rreviewEntity);
        }

        alarmRepository.save(alarmEntity);
    }

    public List<AlarmEntity> FindByKakaoid(Long kakaoid){ //유저로 지원서 SELECT
        return alarmRepository.findByKakaoidOrderByAlTimeAsc(kakaoid);
    }


    public void updateCheckAlarm(Long alarmId){
        AlarmEntity alarm = alarmRepository.findById(alarmId).orElse(null);
        alarm.setConfirm(0);


    }


}
