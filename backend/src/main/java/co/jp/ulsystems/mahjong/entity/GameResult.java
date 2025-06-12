package co.jp.ulsystems.mahjong.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GameResult {
    private Long id;
    private Long gameId;
    private Long playerId;
    private Integer rawScore;
    private Integer rank;
    private Boolean isTobi;
    private Integer finalScore;
    private LocalDateTime createdAt;
}
