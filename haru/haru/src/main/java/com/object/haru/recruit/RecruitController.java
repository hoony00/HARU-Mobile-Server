package com.object.haru.recruit;

import com.object.haru.recruit.dto.RecruitChangeDTO;
import com.object.haru.recruit.dto.RecruitRequestDTO;
import com.object.haru.recruit.dto.ResponseDTO;
import com.object.haru.recruit.dto.SpecificResponseDTO;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 *    구인관련 기능을 담당하는 Controller
 *
 *   @version          1.00    2023.02.01
 *   @author           한승완
 */

@RestController
@RequestMapping(value = "/api/recruit")
public class RecruitController {

    private final RecruitService recruitService;

    public RecruitController(RecruitService recruitService){
        this.recruitService = recruitService;
    }

    @ApiOperation(
            value = "구인글 작성 API + rid 리턴"
            , notes = "RecruitRequestDTO 형식으로 구인글 작성") //(2023-03-17 한승완)
    @PostMapping(value = "/post")
    public void Recruit(@RequestBody RecruitRequestDTO recruitRequestDTO){
        recruitService.SaveRecruit(recruitRequestDTO);
    }

    @ApiOperation(
            value = "구인글 수정 API"
            , notes = "전체다 입력해야 함")  //(2023-02-13 한승완)
    @PutMapping("/change")
    public void ChangeRecruitInfo(RecruitChangeDTO recruitChangeDTO){
        recruitService.ChangeRecruitInfo(recruitChangeDTO);
    }

    @ApiOperation(
            value = "위도 경도 검색할 거리(KM) 선택하여 모집중인 구인 탐색 + 페이징 처리 완료 + 찜 여부"
            , notes = "제목, 분야, 끝나는시간, 돈, 위치, step, 조회수, 찜여부 나옴") //(2023-05-23 한승완)
    @GetMapping("/select/loaction")
    public List<ResponseDTO> SelectRecruit(
            @RequestParam(value = "latitude") Double latitude,
            @RequestParam(value = "longtitude") Double longtitude,
            @RequestParam(value =  "kakaoid") Long kakaoid,
            @RequestParam("page") int page)
    {
        return recruitService.FindAllRecruit(latitude,longtitude,kakaoid,page);
    }

    @ApiOperation(
            value = "내가 작성한 나의 구인 리스트"
            , notes = "step에 상관없이 자신이 작성한 글이면 전부 리턴" ) //(2023-02-14 한승완)
    @GetMapping("/select/user/{kakaoid}")
    public List<ResponseDTO> SelectUid(@PathVariable String kakaoid){
        List<RecruitEntity> recruits = recruitService.FindByUser(Long.parseLong(kakaoid));
        List<ResponseDTO> collect = recruits.stream().map(m -> new ResponseDTO(
                m.getRid(),m.getTitle(),m.getSubject(),m.getStTime(),m.getEndTime(),m.getPay(),m.getAddr()
                ,m.getStep(),m.getCount(),false, m.getRTime(),m.getUid().getKakaoid())).collect(Collectors.toList());
        return collect;
    }

    @ApiOperation(
            value = "내가 작성한 최근 구인글"
            , notes = "자신이 작성한 글 최상단 리턴" ) //(2023-03-15 한승완)
    @GetMapping("/select/user/one/{kakaoid}")
    public ResponseDTO FindOneMyRecruit(@PathVariable Long kakaoid){
        RecruitEntity recruits = recruitService.findTopByUidKakaoidOrderByRid(kakaoid);
        if(recruits != null) {
            return new ResponseDTO(recruits,false);
        }else{
            return null;
        }
    }


    @ApiOperation(
            value = "구인에서 검색어로만 검색"
            , notes = "거리 상관없이 검색어로만" )  //(2023-03-11 한승완)
    @GetMapping("/select/location/search3/{search}/{kakaoid}")
    public List<ResponseDTO> FindBySearch(@PathVariable String search,
                                          @PathVariable Long kakaoid){
        return recruitService.FindBySearch(search,kakaoid);
    }

    @ApiOperation(
            value = "rid로 특정 구인 글 정보 가져오기"
            , notes = "현재 대부분의 정보는 뽑아뒀음 필요한 정보 더 있다면 회신")  //(2023-02-01 한승완)
    @GetMapping("/select/{id}")
    public SpecificResponseDTO Recruitspec(@PathVariable Long id){
        return recruitService.FindOneRecruit(id);
    }


    @ApiOperation(
            value = "rid, kakaoid 로 구인 확정자 뽑기"
            , notes = "구인 테이블의 step = 모집완료 / person= 해당 사용자 이름")  //(2023-02-05 한승완)
    @PutMapping("/choose/{rid}/{kakaoid}")
    public void PickPerson(@PathVariable Long rid,
                           @PathVariable Long kakaoid){
        recruitService.PickPerson(kakaoid,rid);
    }


    @ApiOperation(
            value = "구인글 삭제 API"
            , notes = "DB에서 삭제하지는 않고 step을 삭제로 변경")  //(2023-02-13 한승완)
    @PutMapping("/remove/{rid}")
    public void RemoveRecruitInfo(@PathVariable Long rid){
        recruitService.RemoveRecruit(rid);
    }


    /**
     *  ㅡㅡㅡㅡ 이후 구인관련 내부 DTO ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
     */


    @Data
    @AllArgsConstructor
    static class SearchRequestDTO{  // 검색 + 우치로 내용받는 내부 DTO (2023-02-02 한승완)
        private String search;

        private Double latitude; // 위도

        private Double longtitude; //경도

        private Double distance;

    }

}
