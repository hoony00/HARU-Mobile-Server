package com.object.haru.hreview;

import com.object.haru.hreview.dto.HreviewDTO;
import com.object.haru.hreview.dto.HreviewResponseDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *    가게의 대한 리뷰 관련 기능을 담당하는 Controller
 *
 *   @version          1.00    2023.02.07
 *   @author           이상훈
 */
@RestController
@RequestMapping(value = "/hreview")
public class HreviewController {

    private final HreviewService hreviewService;

    public HreviewController(HreviewService hreviewService) {
        this.hreviewService = hreviewService;
    }

    @ApiOperation(
            value = "대타 알바생의 가게 리뷰 작성 API"
            , notes = "DTO로 리뷰 작성") //(2023-02-06 이상훈) / 리뷰 작성
    @PostMapping("/save")
    public ResponseEntity<HreviewDTO> Save(@RequestBody HreviewDTO hreviewDTO) {
        try {
            hreviewService.SaveHreview(hreviewDTO);
            return ResponseEntity.ok(hreviewDTO);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation(
            value = "hrid로 리뷰 내용 상세 조회 API"
            , notes = "작성자, 수신자, 내용 리턴")  //(2023-02-06 이상훈)
    @GetMapping("/select/detail/{hrid}")
    public HreviewResponseDTO HreviewDetail(@PathVariable Long hrid){
        return hreviewService.getHreviewById(hrid);
    }

    @ApiOperation(
            value = "kakaoid로 리뷰를 썻는지 안썻는지 판별 API"
            , notes = "true 리턴시 리뷰 작성, false 시 미리뷰" +
            "#사용처 1. 해당 글의 대해 리뷰를 썻는지 안썻는지 조회 ")  //(2023-02-10 이상훈)
    @GetMapping("/count/{hreceiverkakaoid}/{hwriterkakaoid}")
    public Boolean countHrid(@RequestParam(value = "hreceiverkakaoid") Long hreceiverkakaoid, @RequestParam(value = "hwriterkakaoid") Long hwriterkakaoid){
        List<HreviewEntity> hreview =  hreviewService.countHrid(hreceiverkakaoid, hwriterkakaoid);
        Long count =  hreview.stream().count();
        if( count == 0){
            return  false;
        }else return true;
    }

    @ApiOperation(
            value = "receiver로 대타 알바를 몇 번 진행 했는지 횟수로 반환 API"
            , notes = "#사용처 1. 해당 사용자가 대타 알바를 몇 번 진행했는지 확인 (리뷰를 몇 번이나 받았는지 횟수 출력) ")  //(2023-02-15 이상훈)
    @GetMapping("/count/{kakaoid}")
    public Long careerHrid(@RequestParam(value = "kakaoid") Long kakaoid){
        List<HreviewEntity> hreview =  hreviewService.careerHrid(kakaoid);
        return   hreview.stream().count();
    }

    @ApiOperation(
            value = "hreceiver의 평점(평균값) 조회 API"
            , notes = "#사용처 1. 해당 사용자의 평균 평점 조회 ")  //(2023-02-15 이상훈)
    @GetMapping("/hrating/{kakaoid}")
    public Double avgHrid(@RequestParam(value = "kakaoid")Long kakaoid){
        return hreviewService.avgHrid(kakaoid);
    }

}

// 리뷰쪽 H/R rating 속성 추가 insert, select, api 수정, 대타 알바 구인, 구직 uid로 조회 api 추가, 평점(평군값) 조회 api 추가
// 궁금한점. 리뷰를 무조건 쓰게 할 것인가? 리뷰를 안쓰더라도 기본 로그를 ( 1. 친절해요, 2. 일을 잘해요 등)사용할 것인가?