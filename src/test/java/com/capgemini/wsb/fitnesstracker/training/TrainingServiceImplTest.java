package com.capgemini.wsb.fitnesstracker.training;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import com.capgemini.wsb.fitnesstracker.training.internal.TrainingRepository;
import com.capgemini.wsb.fitnesstracker.training.internal.TrainingServiceImpl;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.*;

@Transactional
public class TrainingServiceImplTest {
    private TrainingServiceImpl trainingService;

    @Mock
    private TrainingRepository trainingRepository;

    private Training training1;
    private Training training2;
    private Training training3;
    private Training training4;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        trainingService = new TrainingServiceImpl(trainingRepository);

        User user1 = new User("Karol", "Nowak", LocalDate.of(2000, 2, 21), "karol.nowak@gmail.com");
        User user2 = new User("Anna", "Dwornik", LocalDate.of(2000, 8, 5), "anna.dwornik@interia.pl");

        Date startTime = new Date();

        Date endTime1 = new Date(startTime.getTime() + 10000);
        Date endTime2 = new Date(startTime.getTime() + 20000);
        Date endTime3 = new Date(startTime.getTime() + 30000);
        Date endTime4 = new Date(startTime.getTime() + 50000);

        training1 = new Training(user1, startTime, endTime1, ActivityType.SWIMMING, 10.0, 6.0);
        training2 = new Training(user1, startTime, endTime2, ActivityType.RUNNING, 9.0, 9.0);
        training3 = new Training(user2, startTime, endTime3, ActivityType.WALKING, 20.0, 4.0);
        training4 = new Training(user1, startTime, endTime4, ActivityType.RUNNING, 15.0, 7.0);

    }

    @Test
    void testCreateTraining() {
        given(trainingRepository.save(training1)).willReturn(training1);

        Training result = trainingService.createTraining(training1);

        assertThat(result).isEqualTo(training1);
    }

    @Test
    void testGetTraining() {
        Long trainingId = 1L;

        given(trainingRepository.findById(trainingId)).willReturn(Optional.of(training1));
        Optional<Training> result = trainingService.getTraining(trainingId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(training1);
    }

    @Test
    void findAllTrainings() {
        List<Training> trainings = Arrays.asList(training1, training2, training3);

        given(trainingRepository.findAll()).willReturn(trainings);

        List<Training> result = trainingService.findAllTrainings();

        assertEquals(trainings, result);
    }

    @Test
    void findAllTrainingsByUserId() {
        Long userId = 1L;

        List<Training> trainings = Arrays.asList(training1, training2, training4);
        given(trainingRepository.findByUserId(userId)).willReturn(trainings);

        Iterable<Training> result = trainingService.findTrainingsByUserId(userId);

        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyInAnyOrder(training1, training2, training4);
    }

    @Test
    void findAllCompletedTrainingsAfterDate() {
        LocalDateTime date = LocalDateTime.now().minusSeconds(5);

        List<Training> trainings = Arrays.asList(training1, training2, training4);
        given(trainingRepository.findFinishedTrainingsAfter(date)).willReturn(trainings);

        List<Training> result = trainingService.findFinishedTrainingsAfter(date);

        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyInAnyOrder(training1, training2, training4);
    }

    @Test
    void findAllTrainingsByActivityType() {
        ActivityType activityType = ActivityType.RUNNING;
        List<Training> trainings = Arrays.asList(training1, training4);
        given(trainingRepository.findByActivityType(activityType)).willReturn(trainings);

        List<Training> result = trainingService.findTrainingsByActivityType(activityType);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(training1, training4);
    }
}