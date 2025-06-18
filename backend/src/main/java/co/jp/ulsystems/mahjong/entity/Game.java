package co.jp.ulsystems.mahjong.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Game {
    private Long id;
    private Long roomId;
    private LocalDateTime gameDate;
    private Long startingPlayerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
