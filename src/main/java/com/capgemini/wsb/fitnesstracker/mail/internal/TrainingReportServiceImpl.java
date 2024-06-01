package com.capgemini.wsb.fitnesstracker.mail.internal;

import com.capgemini.wsb.fitnesstracker.mail.api.EmailSender;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingService;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingReportServiceImpl {
    private final TrainingService trainingService;
    private final EmailSender emailSender;
    private final UserProvider userProvider;

    public TrainingReportServiceImpl(TrainingService trainingService, UserService userService, EmailSender emailSender, UserProvider userProvider) {
        this.trainingService = trainingService;
        this.emailSender = emailSender;
        this.userProvider = userProvider;
    }

//    @Scheduled(cron = "0/30 * * * * ?") // wysyłąnie co 30 sekund w ramach testu
    @Scheduled(cron = "0 0 8 * * MON") // wysyłąnie co tydzień w poniedziałek o 8.00
    public void sendTrainingReportsWeekly() {
        List<User> users = userProvider.findAllUsers();

        for (User user : users) {
            List<Training> trainings = trainingService.findTrainingsByUserId(user.getId());
            emailSender.send(trainings);
        }
    }
}
