package app.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MatchHistoryResponse {

    @NotNull
    private UUID userId;

    @NotNull
    private String status;

    @NotNull
    private String opponent;

    @NotNull
    private double myPower;

    @NotNull
    private double opponentPower;

    @NotNull
    private int stoneCoins;
}
