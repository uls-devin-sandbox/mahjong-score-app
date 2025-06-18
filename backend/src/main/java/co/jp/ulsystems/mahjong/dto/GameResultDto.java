package co.jp.ulsystems.mahjong.dto;

import lombok.Data;

@Data
public class GameResultDto {
    private Long id;
    private Long gameId;
    private Long playerId;
    private String playerName;
    private Integer rawScore;
    private Integer rank;
    private Boolean isTobi;
    private Integer finalScore;
}
