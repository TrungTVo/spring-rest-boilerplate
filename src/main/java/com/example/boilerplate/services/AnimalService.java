package com.example.boilerplate.services;

import java.util.List;
import java.util.UUID;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.example.boilerplate.commons.dtos.AnimalDTO;
import com.example.boilerplate.commons.dtos.AnimalRequest;
import com.example.boilerplate.interfaces.AnimalInterface;
import com.example.boilerplate.transactions.AnimalTransactions;

@Service
public class AnimalService implements AnimalInterface {
    private Logger logger = LoggerFactory.getLogger(AnimalService.class);
    
    @Value("${instance:A}")
    private String instance;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private AnimalTransactions animalTransactions;

    @Override
    public List<AnimalDTO> getAllAnimals() {
        return this.animalTransactions.getAllAnimals();
    }

    @Override
    public Page<AnimalDTO> getFilteredAnimals(Pageable pageable) {
        return this.animalTransactions.getFilteredAnimals(pageable);
    }

    @Override
    public void saveAnimals() {
        this.animalTransactions.saveAnimals();
    }

    @Override
    @Retryable(
        retryFor    = RuntimeException.class,
        maxAttempts = 3,
        backoff     = @Backoff(delay = 1000, multiplier = 2)
    )
    public AnimalDTO createAnimal(AnimalRequest request) {
        return this.animalTransactions.createAnimal(request);
    }

    @Recover
    public AnimalDTO cannotCreateAnimal(RuntimeException ex, AnimalRequest request) {
        String errorMessage = "Failed to create animal after 3 retries: " + ex.getMessage();
        this.logger.error(errorMessage);
        throw new RuntimeException(errorMessage);
    }

    @Override
    public AnimalDTO getAnimal(UUID animalId) {
        return this.animalTransactions.getAnimal(animalId);
    }

    @Override
    public AnimalDTO updateAnimal(UUID animalId, AnimalRequest request) {
        logger.info("Instance {} is updating animal with ID: {}", instance, animalId);
        boolean locked = false;
        RLock lock = this.redissonClient.getLock("lock:animal:balance:" + animalId.toString());
        try {
            locked = lock.tryLock();
            if (!locked) {
                String errorMessage = "❌ Conflict! Server busy! Instance " + instance + " failed to acquire lock for animal with ID: " + animalId;
                logger.error(errorMessage);
                throw new RuntimeException(errorMessage);
            }
            return this.animalTransactions.updateAnimal(animalId, request);
        }
        finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public void deleteAnimal(UUID animalId) {
        this.animalTransactions.deleteAnimal(animalId);
    }

    @Override
    public AnimalDTO assignHabitat(UUID animalId, UUID habitatId) {
        return this.animalTransactions.assignHabitat(animalId, habitatId);
    }

    @Override
    public AnimalDTO assignMedicalRecord(UUID animalId, UUID recordId) {
        return this.animalTransactions.assignMedicalRecord(animalId, recordId);
    }

    @Override
    public AnimalDTO addCaretaker(UUID animalId, UUID caretakerId) {
        return this.animalTransactions.addCaretaker(animalId, caretakerId);
    }

    @Override
    public AnimalDTO removeCaretaker(UUID animalId, UUID caretakerId) {
        return this.animalTransactions.removeCaretaker(animalId, caretakerId);
    }

}
