package co.jp.ulsystems.mahjong.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GameDto {
    private Long id;
    private Long roomId;
    private LocalDateTime gameDate;
    private Long startingPlayerId;
    private String startingPlayerName;
    private List<GameResultDto> results;
}
