package com.object.haru.fcm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmMessage {
    @JsonProperty("validate_only")
    private boolean validateOnly; // 알림 확인 유무
    private Message message;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private Notification notification;
        private String token;
        private Map<String, String> data; // 추가된 data 필드

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Notification{
        private String title;
        private String body;
     //   private String image;
    }

}
