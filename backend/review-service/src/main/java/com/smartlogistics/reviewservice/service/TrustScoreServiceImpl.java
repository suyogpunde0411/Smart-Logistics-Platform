package com.smartlogistics.reviewservice.service;

import com.smartlogistics.reviewservice.dto.TrustScoreDto;
import com.smartlogistics.reviewservice.entity.ReputationHistory;
import com.smartlogistics.reviewservice.entity.TrustScore;
import com.smartlogistics.reviewservice.events.ReviewKafkaProducer;
import com.smartlogistics.reviewservice.mapper.ReviewMapper;
import com.smartlogistics.reviewservice.repository.ReputationHistoryRepository;
import com.smartlogistics.reviewservice.repository.TrustScoreRepository;
import com.smartlogistics.reviewservice.service.strategy.TrustScoreCalculationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TrustScoreServiceImpl implements TrustScoreService {

    private final TrustScoreRepository trustScoreRepository;
    private final ReputationHistoryRepository historyRepository;
    private final ReviewMapper mapper;
    private final ReviewKafkaProducer kafkaProducer;
    private final TrustScoreCalculationStrategy calculationStrategy;

    public TrustScoreServiceImpl(
            TrustScoreRepository trustScoreRepository,
            ReputationHistoryRepository historyRepository,
            ReviewMapper mapper,
            ReviewKafkaProducer kafkaProducer,
            @Qualifier("weightedTrustScoreStrategy") TrustScoreCalculationStrategy calculationStrategy) {
        
        this.trustScoreRepository = trustScoreRepository;
        this.historyRepository = historyRepository;
        this.mapper = mapper;
        this.kafkaProducer = kafkaProducer;
        this.calculationStrategy = calculationStrategy;
    }

    @Override
    public TrustScoreDto.TrustScoreResponse getTrustScore(UUID userId) {
        TrustScore trustScore = trustScoreRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseGet(() -> createDefaultTrustScore(userId));
        return mapper.toResponse(trustScore);
    }

    @Override
    public List<TrustScoreDto.TrustScoreHistoryResponse> getReputationHistory(UUID userId) {
        List<ReputationHistory> history = historyRepository.findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(userId);
        return mapper.toHistoryResponseList(history);
    }

    @Override
    @Transactional
    public void updateTrustScore(UUID userId, String reason) {
        TrustScore trustScore = trustScoreRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseGet(() -> createDefaultTrustScore(userId));

        int oldScore = trustScore.getScore();
        int newScore = calculationStrategy.calculateScore(trustScore);

        if (oldScore != newScore) {
            trustScore.setScore(newScore);
            trustScoreRepository.save(trustScore);

            ReputationHistory history = ReputationHistory.builder()
                    .userId(userId)
                    .oldScore(oldScore)
                    .newScore(newScore)
                    .reason(reason)
                    .build();
            historyRepository.save(history);

            log.info("Trust score updated for user {}: {} -> {} (Reason: {})", userId, oldScore, newScore, reason);
            kafkaProducer.publishTrustScoreUpdated(userId, oldScore, newScore, reason);
        }
    }

    @Override
    @Transactional
    public void handleTripCompleted(UUID userId) {
        TrustScore trustScore = trustScoreRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseGet(() -> createDefaultTrustScore(userId));
        
        trustScore.setCompletedTrips(trustScore.getCompletedTrips() + 1);
        trustScoreRepository.save(trustScore);
        
        updateTrustScore(userId, "Trip completed bonus");
    }

    @Override
    @Transactional
    public void handleTripCancelled(UUID userId) {
        TrustScore trustScore = trustScoreRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseGet(() -> createDefaultTrustScore(userId));

        trustScore.setCancelledTrips(trustScore.getCancelledTrips() + 1);
        trustScoreRepository.save(trustScore);

        updateTrustScore(userId, "Trip cancelled deduction");
    }

    private TrustScore createDefaultTrustScore(UUID userId) {
        TrustScore trustScore = TrustScore.builder()
                .userId(userId)
                .score(100)
                .averageRating(5.0)
                .completedTrips(0)
                .cancelledTrips(0)
                .reportedReviewsCount(0)
                .disputeCount(0)
                .build();
        return trustScoreRepository.save(trustScore);
    }
}
