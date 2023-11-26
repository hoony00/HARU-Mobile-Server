package com.object.haru.rreview;

import com.object.haru.rreview.dto.RreviewDTO;
import com.object.haru.rreview.dto.RreviewResponseDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *    알바생의 대한 리뷰 관련 기능을 담당하는 Controller
 *
 *   @version          1.00    2023.02.07
 *   @author           이상훈
 */

@RestController
@RequestMapping(value = "/rreview")
public class RreviewController {

    private final RreviewService rreviewService;

    public RreviewController(RreviewService rreviewService) {
        this.rreviewService = rreviewService;
    }

    @ApiOperation(
            value = "사장님/알바생이 대타 일용직의 대한 리뷰 작성 API"
            , notes = "DTO로 리뷰 작성") //(2023-02-22 이상훈) / 리뷰 작성
    @PostMapping("/save")
    public ResponseEntity<RreviewDTO> Save(@RequestBody RreviewDTO rreviewDTO) {
        try {
            rreviewService.SaveRreview(rreviewDTO);
            return ResponseEntity.ok(rreviewDTO);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation(
            value = "rrid로 리뷰 내용 상세 조회 API"
            , notes = "작성자, 수신자, 내용 리턴")  //(2023-02-06 이상훈)
    @GetMapping("/select/detail/{rrid}")
    public RreviewResponseDTO RreviewDetail(@PathVariable Long rrid){
        return rreviewService.getRreviewById(rrid);
    }


    @ApiOperation(
            value = "kakaoid로 리뷰를 썻는지 안썻는지 판별 API"
            , notes = "true 리턴시 리뷰 작성, false 시 미리뷰" +
            "#사용처 1. 해당 글의 대해 리뷰를 썻는지 안썻는지 조회 ")  //(2023-02-10 이상훈)
    @GetMapping("/count/{rreceiverkakaoid}/{rwriterkakaoid}")
    public Boolean countRrid(@RequestParam(value = "rreceiverkakaoid") Long rreceiverkakaoid, @RequestParam(value = "rwriterkakaoid") Long rwriterkakaoid){
        List<RreviewEntity> rreview =  rreviewService.checkRrid(rreceiverkakaoid, rwriterkakaoid);
      Long count =  rreview.stream().count();
      if( count == 0){
          return  false;
      }else return true;
    }

    @ApiOperation(
            value = "receiver로 대타 알바를 몇 번 진행 했는지 횟수로 반환 API"
            , notes = "#사용처 1. 해당 사용자가 대타 알바를 몇 번 진행했는지 확인 ")  //(2023-02-15 이상훈)
    @GetMapping("/count/{rreceiverkakaoid}")
    public Long careerRrid(@RequestParam(value = "rreceiverkakaoid") Long rreceiverkakaoid){
        List<RreviewEntity> rreview =  rreviewService.careerRrid(rreceiverkakaoid);
        return   rreview.stream().count();
    }

    @ApiOperation(
            value = "rreceiver의 평점(평균값) 조회 API"
            , notes = "#사용처 1. 해당 사용자의 평점 조회 ")  //(2023-02-15 이상훈)
    @GetMapping("/rrating/{rreceiverkakaoid}")
    public Double avgRrid(@RequestParam(value = "rreceiverkakaoid")Long rreceiverkakaoid){
        return rreviewService.avgHrid(rreceiverkakaoid);
    }


}