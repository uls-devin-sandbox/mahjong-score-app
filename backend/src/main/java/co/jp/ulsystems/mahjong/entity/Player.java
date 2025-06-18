package co.jp.ulsystems.mahjong.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Player {
    private Long id;
    private String name;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
