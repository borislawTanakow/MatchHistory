package app.ApiTest;

import app.model.MatchHistory;
import app.model.StatusEnum;
import app.service.MatchHistoryService;
import app.web.MatchHistoryController;
import app.web.dto.MatchHistoryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatchHistoryController.class)
public class MatchHistoryControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MatchHistoryService matchHistoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSaveMatchHistory() throws Exception {
        MatchHistoryResponse requestDto = MatchHistoryResponse.builder()
                .status("WIN")
                .opponent("Opponent1")
                .opponentPower(100)
                .myPower(120)
                .userId(UUID.randomUUID())
                .stoneCoins(50)
                .build();

        MatchHistory matchHistory = MatchHistory.builder()
                .status(StatusEnum.WIN)
                .opponent("Opponent1")
                .opponentPower(100)
                .myPower(120)
                .userId(requestDto.getUserId())
                .stoneCoins(50)
                .createdAt(LocalDateTime.now())
                .build();

        // Симулираме поведението
        Mockito.when(matchHistoryService.saveBattle(any(MatchHistoryResponse.class))).thenReturn(matchHistory);

        // Изпълняваме POST заявката към endpoint "/api/v1/history"
        mockMvc.perform(post("/api/v1/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.opponent").value("Opponent1"))
                .andExpect(jsonPath("$.stoneCoins").value(50));
    }

    @Test
    public void testGetHistoryByPlayerId() throws Exception {
        UUID userId = UUID.randomUUID();

        MatchHistory match1 = MatchHistory.builder()
                .userId(userId)
                .opponent("Opponent1")
                .createdAt(LocalDateTime.now())
                .build();
        MatchHistory match2 = MatchHistory.builder()
                .userId(userId)
                .opponent("Opponent2")
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();
        List<MatchHistory> historyList = Arrays.asList(match1, match2);

        // Симулираме поведението
        Mockito.when(matchHistoryService.getHistoryByUserId(userId)).thenReturn(historyList);

        // Изпълняваме GET заявката към endpoint-а "/api/v1/history/{userId}"
        mockMvc.perform(get("/api/v1/history/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].opponent").value("Opponent1"))
                .andExpect(jsonPath("$[1].opponent").value("Opponent2"));
    }
}
