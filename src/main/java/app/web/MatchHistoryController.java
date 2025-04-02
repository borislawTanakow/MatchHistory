package app.web;

import app.model.MatchHistory;
import app.service.MatchHistoryService;
import app.web.dto.MatchHistoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/history")
public class MatchHistoryController {

    private final MatchHistoryService matchHistoryService;

    @Autowired
    public MatchHistoryController(MatchHistoryService matchHistoryService) {
        this.matchHistoryService = matchHistoryService;
    }

    @PostMapping
    public ResponseEntity<MatchHistory> saveMatchHistory(@RequestBody MatchHistoryResponse matchHistoryResponse) {
        MatchHistory matchHistory = this.matchHistoryService.saveBattle(matchHistoryResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(matchHistory);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<MatchHistory>> getHistoryByPlayerId(
            @PathVariable UUID userId) {
        List<MatchHistory> history = matchHistoryService.getHistoryByUserId(userId);
        return ResponseEntity.ok(history);
    }
}
