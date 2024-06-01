package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrainingService {

    List<Training> findAllTrainings();

    List<Training> findTrainingsByUserId(Long userId);

    List<Training> findFinishedTrainingsAfter(LocalDateTime afterTime);

    List<Training> findTrainingsByActivityType(ActivityType activityType);

    Training createTraining(Training training);

    Training updateTraining(Long trainingId, Training training);

    Optional<Training> getTraining(Long trainingId);
}
