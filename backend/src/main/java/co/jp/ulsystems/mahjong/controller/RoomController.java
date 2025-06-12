package co.jp.ulsystems.mahjong.controller;

import co.jp.ulsystems.mahjong.dto.RoomDto;
import co.jp.ulsystems.mahjong.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    
    private final RoomService roomService;
    
    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRooms() {
        List<RoomDto> rooms = roomService.findAll();
        return ResponseEntity.ok(rooms);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long id) {
        RoomDto room = roomService.findById(id);
        return ResponseEntity.ok(room);
    }
    
    @PostMapping
    public ResponseEntity<RoomDto> createRoom(@RequestBody RoomDto roomDto) {
        RoomDto createdRoom = roomService.create(roomDto);
        return ResponseEntity.ok(createdRoom);
    }
    
    @PostMapping("/{roomId}/players/{playerId}")
    public ResponseEntity<Void> addPlayerToRoom(@PathVariable Long roomId, @PathVariable Long playerId) {
        roomService.addPlayerToRoom(roomId, playerId);
        return ResponseEntity.ok().build();
    }
}
