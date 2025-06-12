package co.jp.ulsystems.mahjong.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RuleSetting {
    private Long id;
    private Long roomId;
    private Integer uma1st;
    private Integer uma2nd;
    private Integer uma3rd;
    private Integer uma4th;
    private Integer oka;
    private Integer tobiPenalty;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
