package co.jp.ulsystems.mahjong.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlayerDto {
    private Long id;
    private String name;
    private String nickname;
    private LocalDateTime createdAt;
}
