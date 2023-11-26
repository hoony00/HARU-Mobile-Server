package com.object.haru.fcm;

import com.object.haru.exception.BaseException;
import com.object.haru.fcm.dto.FcmTokenDTO;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/fcm")
public class FcmController {

    @Autowired
    private FirebaseCloudMessageService firebaseCloudMessageService;

    @ApiOperation(
            value = "메세지 보내기 API"
            , notes = "uid, message 내용 받아서 전송 topic은 알림 주제에 따라 newReview, comfirmation, newApply 등을 사용" +
            "id_number는 게시물 번호 보내면 됌 ex) aid가 1인 게시물 -> id_number = 1 ") //(2023-03-21 이상훈)
    @PostMapping("/send")
    public  ResponseEntity<FcmSendDTO> sendMessageToUser(@RequestBody FcmSendDTO fcmSendDTO) {
        try {
            firebaseCloudMessageService.sendNotificationToUser(fcmSendDTO.getKakaoid(), fcmSendDTO.getTitle(), fcmSendDTO.getBody(), fcmSendDTO.getTopic(),"0");
            return ResponseEntity.ok(fcmSendDTO);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }


    }

    @ApiOperation(
            value = "채팅 메세지 보내기 API"
            , notes = "채팅") //(2023-05-11 이상훈)
    @PostMapping("/send/chat")
    public  ResponseEntity<FcmSendDTO> sendChatMessageToUser(@RequestBody FcmSendDTO fcmSendDTO) {
        try {
            firebaseCloudMessageService.sendNotificationToUser(fcmSendDTO.getKakaoid(), fcmSendDTO.getTitle(), fcmSendDTO.getBody(), fcmSendDTO.getTopic(),fcmSendDTO.getId().toString());
            return ResponseEntity.ok(fcmSendDTO);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }


    }

    @ApiOperation(
            value = "fcmToken API"
            , notes = "kakaoid와 개인 fcmToken 입력하여 저장") //(2023-03-21 이상훈)
    @PostMapping("/save")
    public ResponseEntity<FcmTokenDTO> SaveApply(@RequestBody FcmTokenDTO fcmTokenDTO) {
        try {
            firebaseCloudMessageService.overWriteToken(fcmTokenDTO);
            return ResponseEntity.ok(fcmTokenDTO);
        } catch (IllegalArgumentException | BaseException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class FcmSendDTO{
        private Long kakaoid;
        private Long id;
        private String title;
        private String body;
        private String topic;
    }


}
