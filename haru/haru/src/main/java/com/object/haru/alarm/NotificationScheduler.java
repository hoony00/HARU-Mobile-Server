package com.object.haru.alarm;

import com.object.haru.fcm.FirebaseCloudMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class NotificationScheduler {

    @Autowired
    private FirebaseCloudMessageService firebaseCloudMessageService;

    public void scheduleNotification(Long kakaoid, String title, String body, String topic, String id_number, String endTime) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        LocalDateTime endDateTime = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime scheduledDateTime = endDateTime.plusDays(1);
        System.out.println("알람 예약 시간 : " +scheduledDateTime.toString());

        long delay = java.time.Duration.between(LocalDateTime.now(), scheduledDateTime).toMillis();

        executor.schedule(() -> {
            firebaseCloudMessageService.sendNotificationToUser(kakaoid, title, body, topic, id_number);
            System.out.println("예약 알람 전송 ");
        }, delay, TimeUnit.MILLISECONDS);
    }
}
