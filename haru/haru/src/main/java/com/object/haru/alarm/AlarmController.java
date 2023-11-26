package com.object.haru.alarm;


import com.object.haru.alarm.dto.AlarmDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 *    알림 관련 기능을 담당하는 Controller
 *
 *   @version          1.01    2023.03.26
 *   @author           이상훈
 */
@RestController
@RequestMapping(value = "/alarm")
public class AlarmController {
    private final AlarmService alarmService;

    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }



    @ApiOperation(
            value = "kakaoid로 알림 내역 조회 API"
            , notes = "알림 리스트 오래된 순서로 리턴" ) //(2023-05-19 이상훈) / 알림 조회
    @GetMapping("/select/kakaoid")
    public List<AlarmDTO> SelectAlarmList(
            @RequestParam(value = "kakaoid") Long kakaoid){
        List<AlarmEntity> alarm = alarmService.FindByKakaoid(kakaoid);

        List<AlarmDTO> collet = alarm.stream().
                map(m -> new AlarmDTO(  m.getKakaoid(),
                        m.getAlarmid(),
                        m.getBody(),
                        m.getTitle(),
                        m.getConfirm(),
                        m.getAid() != null ? m.getAid().getAid() : 0L,
                        m.getRid() != null ? m.getRid().getRid() : 0L,
                        m.getRrid() != null ? m.getRrid().getRrid() : 0L,
                        m.getAlTime()))
                .collect(Collectors.toList());
        return collet;
    }

    @ApiOperation(
            value = "alarmId로 알림 체크"
            , notes = "안읽음 1 읽음 0")
    @PutMapping("/update/{alarmId}")
    public void updateCheckAlarm(@PathVariable Long alarmId){
        alarmService.updateCheckAlarm(alarmId);

    }

}



    /* 1. alarmid 입력받아서  aid / rid, rrid 리턴해주는 메소드
       2.
     */