package com.object.haru.zzim;

import com.object.haru.recruit.RecruitEntity;
import com.object.haru.recruit.RecruitRepository;
import com.object.haru.rreview.RreviewEntity;
import com.object.haru.user.UserEntity;
import com.object.haru.user.UserRepository;
import com.object.haru.zzim.dto.ZzimRequestDTO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 *    찜 관련 기능을 담당하는 Service
 *
 *   @version          1.00    2023.02.07
 *   @author           이상훈
 */

@Service
@RequiredArgsConstructor // final + not null 생성자 생성 -> 의존성 주입
@Transactional
public class ZzimService {

    private final ZzimRepository zzimRepository;
    private final RecruitRepository recruitRepository;
    private final UserRepository userRepository;


    public void SaveZzimKakao(ZzimRequestDTO zzimRequestDTO){  // 찜누르기
        ZzimEntity zzim = ZzimEntity.builder().
                rid(recruitRepository.getReferenceById (zzimRequestDTO.getRid())).
                uid(userRepository.findByKakaoid (zzimRequestDTO.getKakaoid())).build();
        zzimRepository.save(zzim);
    }
    public List<ZzimEntity> countZzim(Long rid, Long kakaoid){
        return zzimRepository.findByRidRidAndUidKakaoid(rid, kakaoid);
    }

    public List<ZzimEntity> FindByKakaoid(Long kakaoid){ //kakaoid로 지원서 SELECT
        return zzimRepository.findByUidKakaoid(kakaoid);
    }


    public void DeleteZzim(Long rid,Long kakaoid){ zzimRepository.deleteByRidRidAndUidKakaoid(rid, kakaoid); }// 지원서 삭제


}
