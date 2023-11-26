package com.object.haru.fcm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.object.haru.exception.BaseException;
import com.object.haru.exception.BaseResponseStatus;
import com.object.haru.fcm.dto.FcmTokenDTO;
import com.object.haru.user.UserEntity;
import com.object.haru.user.UserRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FirebaseCloudMessageService {

    private final FcmTokenRepository fcmTokenRepository;
    private final UserRepository userRepository;

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/haru-6b737/messages:send";

    private final ObjectMapper objectMapper;


    /**
     * 알림 전송 메서드
     * 알림 전송 필요 시 sendMessageTo(상대방 토큰, 알림 제목, 알림 내용) 호출해 사용
     *
     * @param targetToken
     * @param title
     * @param body
     * @throws IOException
     */

    @Value("classpath:firebase/serviceKey.json")
    private Resource resource;

    //  GoogleCredentials 객체에서 액세스 토큰을 반환
    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(resource.getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        AccessToken accessToken = googleCredentials.getAccessToken();
        if (accessToken == null || accessToken.getTokenValue() == null) {
            throw new IOException("Failed to obtain access token.");
        }
        System.out.println("억세스토큰 메소드 성공");
        return accessToken.getTokenValue();
    }

    // FCM API에서 요구하는 메시지 형식 및 json으로 알림 생성 [title: 알림제목, body: 알림 내용, targetToken: 알림을 받을 대상 토큰]
    private String makeMessage(String targetToken, String title, String body, String topic, String id_number) throws JsonProcessingException {
        Map<String, String> data = new HashMap<>();
        data.put("topic", topic);
        data.put("id", id_number);
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(
                        FcmMessage.Message.builder()
                                .token(targetToken)
                                .notification(
                                        FcmMessage.Notification.builder()
                                                .title(title)
                                                .body(body)
                                                //    .image(null)
                                                .build())
                                .data(data)
                                .build())
                .validateOnly(false)
                .build();
        return objectMapper.writeValueAsString(fcmMessage);
    }
    public void sendMessageTo(FcmMessage fcmMessage, String topic, String id_number) {
        try {
            String message = makeMessage(fcmMessage.getMessage().getToken(),
                    fcmMessage.getMessage().getNotification().getTitle(), fcmMessage.getMessage().getNotification().getBody(),topic, id_number );
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(requestBody)
                    .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("send 메세지 예외 발생 ");
            // 예외 처리 코드 추가
        }
    }

    public ResponseEntity<?> sendNotificationToUser(Long kakaoid, String title, String body,String topic,String id_number) {
        Optional<UserEntity> user = userRepository.FindByKakaoId(kakaoid);
        Long uid = user.get().getUid();
        Optional<FcmTokenEntity> fcmTokenEntity = fcmTokenRepository.findByUid_Uid(uid);
        Map<String, String> data = new HashMap<>();
        data.put("topic", topic);
        data.put("id", id_number);
        if (fcmTokenEntity.isPresent()) {
            FcmTokenEntity token = fcmTokenEntity.get();
            String fcmToken = token.getFcmToken();

            FcmMessage.Notification notification = FcmMessage.Notification.builder()
                    .title(title)
                    .body(body)
                    .build();

            FcmMessage.Message message = FcmMessage.Message.builder()
                    .token(fcmToken)
                    .notification(notification)
                    .data(data)
                    .build();

            FcmMessage fcmMessage = FcmMessage.builder()
                    .validateOnly(false)
                    .message(message)
                    .build();

            sendMessageTo(fcmMessage, topic, id_number);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();

        }
    }

    /*public ResponseEntity<?> sendChatNotificationToUser(Long kakaoid, String title, String body,String topic) {
        Optional<UserEntity> user = userRepository.FindByKakaoId(kakaoid);
        Long uid = user.get().getUid();
        Optional<FcmTokenEntity> fcmTokenEntity = fcmTokenRepository.findByUid_Uid(uid);
        Map<String, String> data = new HashMap<>();
        Map<String, String> idData = new HashMap<>();
        data.put("topic", topic);
        if (fcmTokenEntity.isPresent()) {
            FcmTokenEntity token = fcmTokenEntity.get();
            String fcmToken = token.getFcmToken();

            FcmMessage.Notification notification = FcmMessage.Notification.builder()
                    .title(title)
                    .body(body)
                    .build();

            FcmMessage.Message message = FcmMessage.Message.builder()
                    .token(fcmToken)
                    .notification(notification)
                    .data(data)
                    .build();

            FcmMessage fcmMessage = FcmMessage.builder()
                    .validateOnly(false)
                    .message(message)
                    .build();

            sendMessageTo(fcmMessage, topic);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();

        }
    }*/

    //토큰 엔티티 삭제
    public void deleteToken(UserEntity user) throws BaseException {
        Optional<FcmTokenEntity> optionalFcmTokenEntity = fcmTokenRepository.findByUidUid(user);
        if (optionalFcmTokenEntity.isEmpty()) {
            return;
        }
        FcmTokenEntity fcmTokenEntity = optionalFcmTokenEntity.get();
        try {
            fcmTokenRepository.delete(fcmTokenEntity);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_DELETE_ERROR);
        }
    }

    //front 에서 보낸 fcm 토큰과 서버의 Fcm 토큰이 맞는 지 확인
    public boolean checkToken(FcmTokenDTO fcmDTO) throws BaseException {

        Optional<FcmTokenEntity> optionalFcmTokenEntity = fcmTokenRepository.findByUid_Kakaoid(fcmDTO.getKakaoid());
        if (optionalFcmTokenEntity.isEmpty()) {
            throw new BaseException(BaseResponseStatus.NON_EXIST_FCMTOKEN);
        }
        FcmTokenEntity fcmTokenEntity = optionalFcmTokenEntity.get();

        return Objects.equals(fcmTokenEntity.getFcmToken(), fcmDTO.getFcmToken());
    }

    //토큰이 없으면 save, 있지만 다르면 수정, 있고 같으면 아무일 없음.
    public void overWriteToken(FcmTokenDTO fcmDTO) throws BaseException {
        Optional<FcmTokenEntity> optionalFcmToken = fcmTokenRepository.findByUid_Kakaoid(fcmDTO.getKakaoid());
        System.out.println("로그인시 fcm 토큰 : "+fcmDTO.getFcmToken());
        if (optionalFcmToken.isEmpty()) { // 토큰이 없는 경우
            System.out.println("토큰이 없어서 새로 생성 값 : "+ fcmDTO.getFcmToken());
            SaveFcm(fcmDTO);
        } else if (!checkToken(fcmDTO)) {  //토큰이 다른경우
            System.out.println("토큰이 있음 기존 값 : "+ optionalFcmToken.get().getFcmToken());
            System.out.println("토큰이 있음 새로 받은 값 : "+fcmDTO.getFcmToken());
            FcmTokenEntity fcmTokenEntity = optionalFcmToken.get();
            fcmTokenEntity.updateToken(fcmDTO.getFcmToken());
            fcmTokenRepository.save(fcmTokenEntity);
        }
    }


    public void SaveFcm(FcmTokenDTO fcmDTO) throws BaseException {
        FcmTokenEntity fcmTokenEntity = FcmTokenEntity.builder().
                fcmToken(fcmDTO.getFcmToken()).
                uid(userRepository.findByKakaoid(fcmDTO.getKakaoid())).build();
        try {
            fcmTokenRepository.save(fcmTokenEntity);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
    }



}

