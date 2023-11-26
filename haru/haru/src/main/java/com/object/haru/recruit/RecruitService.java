package com.object.haru.recruit;

import com.object.haru.alarm.AlarmService;
import com.object.haru.alarm.NotificationScheduler;
import com.object.haru.fcm.FirebaseCloudMessageService;
import com.object.haru.recruit.dto.RecruitChangeDTO;
import com.object.haru.recruit.dto.RecruitRequestDTO;

import javax.transaction.Transactional;

import com.object.haru.recruit.dto.ResponseDTO;
import com.object.haru.recruit.dto.SpecificResponseDTO;
import com.object.haru.user.UserEntity;
import com.object.haru.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *    구인관련 기능을 담당하는 Service
 *
 *   @version          1.00    2023.02.01
 *   @author           한승완
 */

@Service
@RequiredArgsConstructor // final + not null 생성자 생성 -> 의존성 주입
@Transactional
public class RecruitService {

    private final RecruitRepository recruitRepository;

    private final UserRepository userRepository;


    private final FirebaseCloudMessageService firebaseCloudMessageService;

    private final AlarmService alarmService;
    private final NotificationScheduler notificationScheduler;



    //DTO로 받아서 엔티티 저장 (2023-01-31 한승완)
    public RecruitEntity SaveRecruit(RecruitRequestDTO recruitRequestDTO) {

        RecruitEntity recruit = RecruitEntity.builder().
                rage(recruitRequestDTO.getRage()).
                endTime(recruitRequestDTO.getEndTime()).
                stTime(recruitRequestDTO.getStTime()).
                pay(recruitRequestDTO.getPay()).
                title(recruitRequestDTO.getTitle()).
                rcareer(recruitRequestDTO.getRcareer()).
                subject(recruitRequestDTO.getSubject()).
                step("모집중").
                addr(recruitRequestDTO.getAddr()).
                lat(recruitRequestDTO.getLat()).
                lon(recruitRequestDTO.getLon()).
                uid(userRepository.findByKakaoid(recruitRequestDTO.getKakaoid())).
                rsex(recruitRequestDTO.getRsex()).build();
        return recruitRepository.save(recruit);
    }

    public void ChangeRecruitInfo(RecruitChangeDTO recruitChangeDTO){
        Optional<RecruitEntity> recruit = recruitRepository.findById(recruitChangeDTO.getRid());
        if(recruit.isPresent()){
            recruit.get().setTitle(recruitChangeDTO.getTitle());
            recruit.get().setSubject(recruitChangeDTO.getSubject());
            recruit.get().setStTime(recruitChangeDTO.getStTime());
            recruit.get().setEndTime(recruitChangeDTO.getEndTime());
            recruit.get().setPay(recruitChangeDTO.getPay());
            recruit.get().setAddr(recruitChangeDTO.getAddr());
            recruit.get().setLat(recruitChangeDTO.getLat());
            recruit.get().setLon(recruitChangeDTO.getLon());
            recruit.get().setRage(recruitChangeDTO.getRage());
            recruit.get().setRsex(recruitChangeDTO.getRsex());
            recruit.get().setRcareer(recruitChangeDTO.getRcareer());
        }
    }

    public RecruitEntity findTopByUidKakaoidOrderByRid(Long kakaoid){
        Optional<RecruitEntity> recruit = recruitRepository.findTopByUidKakaoidOrderByRidDesc(kakaoid);
        if(recruit.isPresent()){
            return recruit.get();
        }else{
            return null;
        }
    }

    public List<ResponseDTO> FindBySearch(String search, Long kakaoid){

        List<RecruitEntity> recruits =  recruitRepository.findBySearchWithZzims(search);
        UserEntity user = userRepository.findByKakaoid(kakaoid);

        return recruits.stream()
                .map(recruit -> {
                    boolean hasZzim = recruit.getZzims().stream()
                            .anyMatch(zzim -> zzim.getUid().equals(user));
                    return new ResponseDTO(recruit, hasZzim);
                })
                .collect(Collectors.toList());
    }

    public List<ResponseDTO> FindAllRecruit(Double lat, Double lon, Long kakaoid, int page) {
        int pageSize = 20; // 페이지당 결과 수
        Pageable pageable = PageRequest.of(page, pageSize);

        List<RecruitEntity> recruits = recruitRepository.findAllRecruitWithZzims(lat + lon, pageable);
        UserEntity user = userRepository.findByKakaoid(kakaoid);

        return recruits.stream()
                .map(recruit -> {
                    boolean hasZzim = recruit.getZzims().stream()
                            .anyMatch(zzim -> zzim.getUid().equals(user));
                    return new ResponseDTO(recruit, hasZzim);
                })
                .collect(Collectors.toList());
    }



    public SpecificResponseDTO FindOneRecruit(Long rid){
       Optional<RecruitEntity> recruit = recruitRepository.findById(rid);
       if(recruit.isPresent()) {
           SpecificResponseDTO specificResponseDTO = new SpecificResponseDTO(recruit);
           recruit.get().setCount(recruit.get().getCount()+1);
           return specificResponseDTO;
       }else{
           return new SpecificResponseDTO();
       }
    }

    //확정
    public void PickPerson(Long kakaoid, Long rid){

        RecruitEntity recruit = recruitRepository.findById(rid).orElse(null);
        recruit.setPerson(kakaoid);
        recruit.setStep("모집완료");
        recruit.getEndTime();

        String title = "지원하신 알바가 확정되었습니다!";


        notificationScheduler.scheduleNotification(kakaoid,recruit.getTitle(), "리뷰를 작성해주세요!","comfirmation",rid.toString(),recruit.getEndTime());

        //알림 보내기
        firebaseCloudMessageService.sendNotificationToUser(kakaoid,title, recruit.getTitle(),"comfirmation",rid.toString());

        alarmService.saveAlarm(title, recruit.getTitle(),kakaoid, recruit.getRid(),"rid");

    }

    public List<RecruitEntity> FindByUser(Long kakaoid){
        return recruitRepository.findByUidKakaoidOrderByRidAsc(kakaoid);
    }

    public void RemoveRecruit(Long rid){
        RecruitEntity recruit = recruitRepository.findById(rid).orElse(null);
        recruit.setStep("삭제");
    }



}
