package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.TrainingNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RelationTypeNotFoundException;

@RestController
@RequestMapping("/v1/training")
public class TrainingController {
    private final TrainingServiceImpl trainingService;

    private final TrainingMapper trainingMapper;

    TrainingController(TrainingServiceImpl trainingService, TrainingMapper trainingMapper) {
        this.trainingService = trainingService;
        this.trainingMapper = trainingMapper;
    }

    @GetMapping("/{trainingId}")
    public TrainingDto getTraining(@PathVariable Long trainingId) throws TrainingNotFoundException {
        return trainingService.getTraining(trainingId)
                .map(trainingMapper::toDto).orElseThrow();
//        return trainingService.getTraining(trainingId)
//                .map(trainingMapper::toDto)
//                .stream()
//                .toList();
    }



    @PostMapping
    public void addTraining(@RequestBody TrainingDto trainingDto) throws InterruptedException{


        System.out.println("User with e-mail: " + trainingDto + "passed to the request");

        trainingService.createTraining(this.trainingMapper.toEntity(trainingDto));

    }
}
