package com.safety.eyekeep.user.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SendMessageService {
    public String sendEyeKeepMessage(String fcmToken, String username, String nickname) throws FirebaseMessagingException {

        // 메시지 만들기
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("EYEKEEP")
                        .setBody(nickname + "(" + username + ")님의 아이킵 등록 요청이 왔습니다.")
                        .build())
                .setToken(fcmToken)
                .putData("email", username)
                .putData("nickname", nickname)
                .build();

        return FirebaseMessaging.getInstance().send(message);
    }

    public String sendEmergencyMessage(String fcmToken, String nickname) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("EMERGENCY")
                        .setBody(nickname + "님의 아이캐쳐에서 위급신호가 왔습니다.")
                        .build())
                .setToken(fcmToken)
                .build();

        return FirebaseMessaging.getInstance().send(message);
    }
}