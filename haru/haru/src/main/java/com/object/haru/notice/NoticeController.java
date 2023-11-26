package com.object.haru.notice;

import com.object.haru.notice.dto.NoticeDTO;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 *    공지관련 기능을 담당하는 Controller
 *
 *   @version          1.00    2023.04.12
 *   @author           한승완
 */

@RestController
@RequestMapping(value = "/admin/notice")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService){
        this.noticeService = noticeService;
    }

    @ApiOperation(
            value = "공지 작성"
            , notes = "내용 담아서 공지 작성") //(2023-04-12 한승완)
    @PostMapping(value = "/post")
    public void SaveNotice(@RequestBody NoticeDTO noticeDTO){
        noticeService.SaveNoticeService(noticeDTO);
    }

    @ApiOperation(
            value = "전체 공지"
            , notes = "nid + ncontents" )  //(2023-04-14 한승완)
    @GetMapping("/select")
    public List<ResponseDTO> SelectNotice(){
        List<NoticeEntity> notice = noticeService.SelectAllNotice();
        List<ResponseDTO> col = notice.stream().map(m -> new ResponseDTO(
                m.getNid() , m.getNcontent())).collect(Collectors.toList());
        return col;
    }

    @Data
    @AllArgsConstructor
    static class ResponseDTO{
        private Long nid;
        private String ncontents;
    }


    }
