package com.object.haru.zzim;

import com.object.haru.zzim.dto.ZzimRequestDTO;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 *    구인글의 대한 찜 관련 기능을 담당하는 Controller
 *
 *   @version          1.00    2023.02.07
 *   @author           이상훈
 */
@RestController
@RequestMapping(value = "/zzim/v1")
public class ZzimController {

    private final ZzimService zzimService;

    public ZzimController(ZzimService zzimService) {
        this.zzimService = zzimService;
    }

    @ApiOperation(
            value = "찜 누르기 API"
            , notes = "Kakaoid, rid 받아서 찜 작성" +
            "@ 리턴값의 따른 에러 (0) 찜 작성 실패" +
            "                   (1) 찜 작성 성공 " +
            "#사용처 1. 찜누르기 ") //(2023-02-03 이상훈) / 찜 누르기
    @PostMapping("/save")
    public void zzimSave(@RequestBody ZzimRequestDTO zzimRequestDTO ){
        zzimService.SaveZzimKakao(zzimRequestDTO);

    }

    @ApiOperation(
            value = "kakaoid로 찜 내역(리스트) 상세조회 API"
            , notes = "return값 리스트와 동일" +
            "#사용처 1. 자신의 찜 리스트 ") //(2023-03-17 이상훈, 한승완) / 내가 찜한 리스트
    @GetMapping("/select/kakaoid")
    public List<ResponseUidDTO> SelectKakaoZzim(
            @RequestParam(value = "kakaoid") Long kakaoid
    ){
        List<ZzimEntity> apply = zzimService.FindByKakaoid(kakaoid);
        List<ResponseUidDTO> collet = apply.stream().
                map(m -> new ResponseUidDTO(
                        m.getRid().getRid(),m.getRid().getTitle(),m.getRid().getSubject(),m.getRid().getRTime(),m.getRid().getStTime(),
                        m.getRid().getEndTime(),m.getRid().getPay(),m.getRid().getAddr(),m.getRid().getStep(),
                        m.getRid().getCount()
                        ,true,m.getRid().getUid().getKakaoid())).collect(Collectors.toList());
        return collet;
    }


    @ApiOperation(
            value = " 찜 취소 API"
            , notes = "kakaoid, rid를 받아서 찜 취소(삭제)" +
            "#사용처 1. 자신이 누른 찜(꽉찬 하트(?))에 누를시 이 메소드 실행하여 취소") //(2023-02-03 이상훈) / 찜 취소
    @DeleteMapping("/delete/{kakaoid}/{rid}")
    public void DeleteZzim(@RequestParam(value = "rid") Long rid, @RequestParam(value = "kakaoid") Long kakaoid){
        zzimService.DeleteZzim(rid, kakaoid);
    }

    @ApiOperation(
            value = "rid, kakaoid로 찜 여부 확인 API"
            , notes = "true 리턴시 찜 완료, false 시 찜 미완료" +
            "#사용처 1. 구인글에 자신의 찜 여부 확인")  //(2023-02-10 이상훈)
    @GetMapping("/count/{rid}/{kakaoid}")
    public boolean CheckZzim(@RequestParam(value = "rid") Long rid, @RequestParam(value = "kakaoid") Long kakaoid){
        List<ZzimEntity> zzim =  zzimService.countZzim(rid, kakaoid);
        Long count =  zzim.stream().count();
        if( count == 0){
            return  false;
        }else return true;
    }

    @Data
    @AllArgsConstructor
    static class ResponseUidDTO{ // 유저_응답형태를위한 내부 DTO
        private Long rid;
        private String title; // 제목
        private String subject; // 분야
        private LocalDateTime rTime;
        private String stTime;
        private String endTime; // 끝나는 시간
        private int pay; // 돈
        private String addr; // 주소 = 위도+경도라고 생각됨
        private String step;
        private int count;
        private boolean zzim;
        private Long kakaoid;
    }



}
