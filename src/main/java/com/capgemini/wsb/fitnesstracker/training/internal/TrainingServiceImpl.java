package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingService;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
// TODO: Provide Impl
public class TrainingServiceImpl implements TrainingProvider, TrainingService {

    private final TrainingRepository trainingRepository;
    TrainingServiceImpl(TrainingRepository trainingRepository){this.trainingRepository=trainingRepository;}

    @Override
    public Training createTraining(final Training training){
        System.out.printf("Training Created %s", training);
        if (training.getId() != null){
            throw new IllegalArgumentException("trainig cos tam");
        }
        return trainingRepository.save(training);
    }

    @Override
    public Optional<Training> getTraining(final Long trainingId) {
        return trainingRepository.findById(trainingId);
//        throw new UnsupportedOperationException("Not finished yet");


    }

//    public Training saveTraining(TrainingDto trainingDto) {
//        Training training = new Training(trainingDto.startTime(), trainingDto.endTime(), trainingDto.activityType(), trainingDto.distance(), trainingDto.averageSpeed());
//
//        Training savedTraining = trainingRepository.save(training);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedTraining).getBody();
//    }
}
