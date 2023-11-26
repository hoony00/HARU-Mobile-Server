package com.object.haru.apply;

import com.object.haru.apply.dto.ApplyRequestDTO;
import com.object.haru.apply.dto.DetailResponseDTO;
import com.object.haru.apply.dto.ResponseRidDTO;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *    구직 지원서 관련 기능을 담당하는 Controller
 *
 *   @version          1.0    2023.02.08
 *   @author           이상훈
 */

@RestController
@RequestMapping(value = "/apply/v1")
public class ApplyController {

    private final ApplyService applyService;
    public ApplyController(ApplyService applyService) {
        this.applyService = applyService;
    }



    @ApiOperation(
            value = "지원서 작성 API"
            , notes = "kakaoid를 받아서 지원서 작성하면 uid가 저장") //(2023-02-01 이상훈) / 지원서 작성
    @PostMapping("/save")
    public ResponseEntity<ApplyRequestDTO> SaveApply(@RequestBody ApplyRequestDTO applyRequestDTO) {
        try {
            applyService.SaveApply(applyRequestDTO);
            return ResponseEntity.ok(applyRequestDTO);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }


    @ApiOperation(
            value = "구인글 번호로 지원서 조회 API"
            , notes = "해당 구인글의 지원한 사람들의 리스트 리턴(구인글 제목, 지원서 자기소개, aid(지원서 번호), uid(유저 번호), rid(구인글 번호)" +
            "#사용처 1. 나의 리스트에서 지원자 리스트 조회") //(2023-02-01 이상훈) / rid 지원서 조회 / 지원자 리스트 조회
    @GetMapping("/select/rid")
    public List<ResponseRidDTO> selectRidApply(@RequestParam(value = "rid") Long rid) {
        List<ResponseRidDTO> result = applyService.findByRidWithRating(rid);
        return result;
    }

    @ApiOperation(
            value = "유저 아이디로 지원서 조회 API"
            , notes = "해당 유저가 지원한 지원서 리스트 리턴(지원서 제목, 자기소개, 이름, aid(지원서 번호), uid(유저 번호), rid(구인글 번호)" +
            "#사용처 1. 니의 리스트에서 나의 지원 리스트 조회") //(2023-02-08 이상훈) / uid 지원서 조회
    @GetMapping("/select/kakaoid")
    public List<com.object.haru.recruit.dto.ResponseDTO> SelectUidAplly(
            @RequestParam(value = "kakaoid") Long kakaoid){
        return  applyService.FindByKakaoid(kakaoid);
    }

    @ApiOperation(
            value = "최근에 지원한 지원글 API"
            , notes = "kakaoid로 입력")  //(2023-02-05 이상훈)
    @GetMapping("/recentApply")
    public com.object.haru.recruit.dto.ResponseDTO getRecentApply(@RequestParam("kakaoid") Long kakaoid) {
        return applyService.getRecentApply(kakaoid);
    }

    @ApiOperation(
            value = " 지원서 삭제 API"
            , notes = "지원서 번호로 지원취소") //(2023-02-02 이상훈) / 지원서 삭제
    @DeleteMapping("/delete/aid")
    public void DeleteAplly(@RequestParam(value = "aid") Long aid){
        applyService.deleteApply(aid);
    }

    @ApiOperation(
            value = "지원서 상세내역 API"
            , notes = "지원자 평점 조회는 추후 추가")  //(2023-02-05 이상훈)
    @GetMapping("/select/detail/{aid}")
    public DetailResponseDTO ApplyDetail(@RequestParam(value = "aid") Long aid) {
        return applyService.FindOneApply(aid);
    }

    @ApiOperation(
            value = "rid, kakaoid로 지원서를 썻는지 안썻는지 판별 API"
            , notes = "지원서를 썼을경우 aid 반환 아닐경우 false 반환" +
            "#사용처 1. 해당 글의 지원서를 썻는지 안썻는지 조회 ")  //(2023-02-10 이상훈)
    @GetMapping("/count/{rid}/{kakaoid}")
    public Long countApply(@RequestParam(value = "rid") Long rid, @RequestParam(value = "kakaoid") Long kakaoid){
        Optional<ApplyEntity> apply =  applyService.countApply(rid, kakaoid);
        if(apply.isPresent()){
            return apply.get().getAid();
        }else{
            return 0L;
        }
    }

    @ApiOperation(
            value = "rid, kakaoid로 지원서의 내용 리턴 API"
            , notes = "kakaoid 사용자가 해당 rid의 글에 작성한 지원서 정보 리턴" )  //(2023-04-02 이상훈)
    @GetMapping("/info/{rid}/{kakaoid}")
    public ResponseDTO RecentApply(@RequestParam(value = "rid") Long rid, @RequestParam(value = "kakaoid") Long kakaoid){
       ApplyEntity apply =  applyService.recentApply(rid, kakaoid);
       if(apply != null){
           return  new ResponseDTO(apply);
       }else {
           return null;
       }

    }


    @Data
    @AllArgsConstructor
    static class ResponseUidDTO{ // Uid 유저_응답형태를위한 내부 DTO
        private String title; //구인글 제목
        private String myself; // 자기소개
        private String name;
        private Long aid;
        private Long uid;
        private Long rid;
        private String step;
    }

    @Data
    @AllArgsConstructor
    static class ResponseDTO{ // Uid 유저_응답형태를위한 내부 DTO
        private Long aid;
        private Long uid;
        private Long rid;
        private String myself; // 자기소개
        private String aage;
        private String acareer;
        private String asex;
        private LocalDateTime aTime;

        public  ResponseDTO(ApplyEntity apply) {
            this.aage = apply.getAage();
            this.acareer = apply.getAcareer();
            this.asex = apply.getAsex();
            this.myself = apply.getMyself();
            this.aid = apply.getAid();
            this.rid = apply.getRid().getRid();
            this.uid = apply.getUid().getUid();
            this.aTime = apply.getATime();

        }
    }

    
}
