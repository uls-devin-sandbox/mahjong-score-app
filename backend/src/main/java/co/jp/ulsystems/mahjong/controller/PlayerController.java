package co.jp.ulsystems.mahjong.controller;

import co.jp.ulsystems.mahjong.dto.PlayerDto;
import co.jp.ulsystems.mahjong.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {
    
    private final PlayerService playerService;
    
    @GetMapping
    public ResponseEntity<List<PlayerDto>> getAllPlayers() {
        List<PlayerDto> players = playerService.findAll();
        return ResponseEntity.ok(players);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> getPlayerById(@PathVariable Long id) {
        PlayerDto player = playerService.findById(id);
        return ResponseEntity.ok(player);
    }
    
    @PostMapping
    public ResponseEntity<PlayerDto> createPlayer(@RequestBody PlayerDto playerDto) {
        PlayerDto createdPlayer = playerService.create(playerDto);
        return ResponseEntity.ok(createdPlayer);
    }
}
