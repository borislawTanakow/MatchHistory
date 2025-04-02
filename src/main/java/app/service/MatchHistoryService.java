package app.service;

import app.model.MatchHistory;
import app.model.StatusEnum;
import app.repository.MatchHistoryRepository;
import app.web.dto.MatchHistoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MatchHistoryService {

    private final MatchHistoryRepository matchHistoryRepository;

    @Autowired
    public MatchHistoryService(MatchHistoryRepository matchHistoryRepository) {
        this.matchHistoryRepository = matchHistoryRepository;
    }

    public MatchHistory saveBattle(MatchHistoryResponse matchHistoryDto) {

        MatchHistory matchHistory = MatchHistory.builder()
                .status(StatusEnum.valueOf(matchHistoryDto.getStatus()))
                .opponent(matchHistoryDto.getOpponent())
                .opponentPower(matchHistoryDto.getOpponentPower())
                .myPower(matchHistoryDto.getMyPower())
                .userId(matchHistoryDto.getUserId())
                .stoneCoins(matchHistoryDto.getStoneCoins())
                .createdAt(LocalDateTime.now())
                .build();


        matchHistoryRepository.save(matchHistory);

            return matchHistory;
    }

    public List<MatchHistory> getHistoryByUserId(UUID userId) {
        return matchHistoryRepository.findTop6ByUserIdOrderByCreatedAtDesc(userId);    }
}
