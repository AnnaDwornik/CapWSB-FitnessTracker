package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Objects;
import java.util.Optional;

interface TrainingRepository extends JpaRepository<Training, Long> {
    default Optional<Training> findById(String id) {
        return findAll().stream()
                .filter(training -> Objects.equals(Long.toString(training.getId()), id))
                .findFirst();
    }
}
