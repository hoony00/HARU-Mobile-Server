package com.object.haru.notice;

import javax.transaction.Transactional;
import com.object.haru.notice.dto.NoticeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // final + not null 생성자 생성 -> 의존성 주입
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public void SaveNoticeService(NoticeDTO noticeDTO){
        noticeRepository.save(new NoticeEntity(null,noticeDTO.getNcontent()));
    }

    public List<NoticeEntity> SelectAllNotice(){
        return noticeRepository.findAll();
    }

}
