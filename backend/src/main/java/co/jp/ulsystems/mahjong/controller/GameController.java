package co.jp.ulsystems.mahjong.controller;

import co.jp.ulsystems.mahjong.dto.CreateGameRequest;
import co.jp.ulsystems.mahjong.dto.GameDto;
import co.jp.ulsystems.mahjong.dto.GameResultDto;
import co.jp.ulsystems.mahjong.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {
    
    private final GameService gameService;
    
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<GameDto>> getGamesByRoomId(@PathVariable Long roomId) {
        List<GameDto> games = gameService.findByRoomId(roomId);
        return ResponseEntity.ok(games);
    }
    
    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<GameResultDto>> getGamesByPlayerId(@PathVariable Long playerId) {
        List<GameResultDto> results = gameService.findByPlayerId(playerId);
        return ResponseEntity.ok(results);
    }
    
    @PostMapping
    public ResponseEntity<GameDto> createGame(@RequestBody CreateGameRequest request) {
        GameDto createdGame = gameService.create(request);
        return ResponseEntity.ok(createdGame);
    }
}
