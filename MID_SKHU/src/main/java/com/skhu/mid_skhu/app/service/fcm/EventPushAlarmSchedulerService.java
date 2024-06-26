package com.skhu.mid_skhu.app.service.fcm;

import com.skhu.mid_skhu.app.dto.user.responseDto.UserTodoListResponseDto;
import com.skhu.mid_skhu.app.dto.user.responseDto.UserTodoListWrapperResponseDto;
import com.skhu.mid_skhu.app.entity.student.Student;
import com.skhu.mid_skhu.app.service.fcm.alarmInterface.FirebaseCloudMessageService;
import com.skhu.mid_skhu.app.service.fcm.alarmInterface.StudentTodayTodoListIsExitService;
import com.skhu.mid_skhu.app.service.fcm.alarmInterface.UserTodayTodoListCheckService;
import com.skhu.mid_skhu.global.common.dto.ApiResponseTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class EventPushAlarmSchedulerService {

    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final UserTodayTodoListCheckService userTodayTodoListCheckService;
    private final StudentTodayTodoListIsExitService studentTodayTodoListIsExitService;

    @Scheduled(cron = "0 0 1 * * *")
    public void sendEventPushAlarm() throws IOException {
        List<Student> students = getStudentsTodayTodoList();

        sendNotificationsForTodayEvents(students);
    }

    private List<Student> getStudentsTodayTodoList() {
        return studentTodayTodoListIsExitService.getStudentListExistTodayTodoList();
    }

    private void sendNotificationsForTodayEvents(List<Student> students) throws IOException {
        for (Student student : students) {
            Principal principal = () -> String.valueOf(student.getUserId());
            ApiResponseTemplate<UserTodoListWrapperResponseDto> response = userTodayTodoListCheckService.checkTodayTodoList(principal);

            if (!response.isSuccess()) {
                List<UserTodoListResponseDto> eventList = response.getData().getResponseDto();

                for (UserTodoListResponseDto eventDto : eventList) {
                    if (shouldSendNotification(eventDto)) {
                        String fcmToken = student.getFcmToken();
                        if (fcmToken != null) {
                            String[] message = createNotificationMessage(eventDto);
                            sendNotificationToStudent(fcmToken, message[0], message[1]);
                        }
                    }
                }
            }
        }
    }

    private boolean shouldSendNotification(UserTodoListResponseDto eventDto) {
        LocalDateTime eventStartAt = eventDto.getStartAt();
        LocalDateTime threeHoursBefore = eventStartAt.minusHours(3);
        LocalDateTime now = LocalDateTime.now();

        return now.isAfter(threeHoursBefore) && now.isBefore(eventStartAt);
    }

    private String[] createNotificationMessage(UserTodoListResponseDto eventDto) {
        String title = "오늘 하루 일정 알림";
        String body = "[" + eventDto.getEventTitle() + "]" + " 일정이 3시간 뒤에 관심사에 맞는 일정이 있어요!";
        return new String[]{title, body};
    }

    private void sendNotificationToStudent(String fcmToken, String title, String body) throws IOException {
        firebaseCloudMessageService.sendMessageTo(fcmToken, title, body);
    }
}
