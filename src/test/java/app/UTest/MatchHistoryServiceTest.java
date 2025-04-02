package app.UTest;

import app.model.MatchHistory;
import app.model.StatusEnum;
import app.repository.MatchHistoryRepository;
import app.service.MatchHistoryService;
import app.web.dto.MatchHistoryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchHistoryServiceTest {

    @Mock
    private MatchHistoryRepository matchHistoryRepository;


    @InjectMocks
    private MatchHistoryService matchHistoryService;

    @Test
    public void testSaveBattle() {

        MatchHistoryResponse matchHistoryResponse = MatchHistoryResponse.builder()
                .status("WIN")
                .opponent("Opponent1")
                .opponentPower(100)
                .myPower(120)
                .userId(UUID.randomUUID())
                .stoneCoins(50)
                .build();


        MatchHistory savedMatchHistory = MatchHistory.builder()
                .status(StatusEnum.valueOf(matchHistoryResponse.getStatus()))
                .opponent(matchHistoryResponse.getOpponent())
                .opponentPower(matchHistoryResponse.getOpponentPower())
                .myPower(matchHistoryResponse.getMyPower())
                .userId(matchHistoryResponse.getUserId())
                .stoneCoins(matchHistoryResponse.getStoneCoins())
                .createdAt(LocalDateTime.now())
                .build();

        when(matchHistoryRepository.save(any(MatchHistory.class))).thenReturn(savedMatchHistory);

        // Извикване на тествания метод
        MatchHistory result = matchHistoryService.saveBattle(matchHistoryResponse);

        // Проверки
        verify(matchHistoryRepository, times(1)).save(any(MatchHistory.class));
        assertEquals(matchHistoryResponse.getOpponent(), result.getOpponent());
        assertEquals(matchHistoryResponse.getOpponentPower(), result.getOpponentPower());
        assertEquals(matchHistoryResponse.getMyPower(), result.getMyPower());
        assertEquals(matchHistoryResponse.getUserId(), result.getUserId());
        assertEquals(matchHistoryResponse.getStoneCoins(), result.getStoneCoins());
        assertEquals(StatusEnum.valueOf(matchHistoryResponse.getStatus()), result.getStatus());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    public void testGetHistoryByUserId() {

        UUID userId = UUID.randomUUID();
        MatchHistory match1 = MatchHistory.builder()
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build();
        MatchHistory match2 = MatchHistory.builder()
                .userId(userId)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();
        List<MatchHistory> matchHistoryList = Arrays.asList(match1, match2);

        when(matchHistoryRepository.findTop6ByUserIdOrderByCreatedAtDesc(userId)).thenReturn(matchHistoryList);

        // Извикване на тествания метод
        List<MatchHistory> resultList = matchHistoryService.getHistoryByUserId(userId);

        // Проверки
        verify(matchHistoryRepository, times(1)).findTop6ByUserIdOrderByCreatedAtDesc(userId);
        assertEquals(2, resultList.size());
        assertEquals(userId, resultList.get(0).getUserId());
    }
}
