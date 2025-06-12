package co.jp.ulsystems.mahjong.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoomDto {
    private Long id;
    private String name;
    private String yearMonth;
    private LocalDateTime createdAt;
    private List<PlayerDto> players;
    private RuleSettingDto ruleSetting;
}
