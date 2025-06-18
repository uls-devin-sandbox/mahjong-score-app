package co.jp.ulsystems.mahjong.dto;

import lombok.Data;

@Data
public class RuleSettingDto {
    private Long id;
    private Long roomId;
    private Integer uma1st;
    private Integer uma2nd;
    private Integer uma3rd;
    private Integer uma4th;
    private Integer oka;
    private Integer tobiPenalty;
}
