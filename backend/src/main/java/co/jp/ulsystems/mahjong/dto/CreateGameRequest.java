package co.jp.ulsystems.mahjong.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class CreateGameRequest {
    private Long roomId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime gameDate;
    private Long startingPlayerId;
    private List<GameResultRequest> results;
    
    @Data
    public static class GameResultRequest {
        private Long playerId;
        private Integer rawScore;
        private Boolean isTobi;
    }
}
