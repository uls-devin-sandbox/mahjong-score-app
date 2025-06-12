package co.jp.ulsystems.mahjong.mapper;

import co.jp.ulsystems.mahjong.entity.RuleSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RuleSettingMapper {
    RuleSetting findByRoomId(@Param("roomId") Long roomId);
    void insert(RuleSetting ruleSetting);
    void update(RuleSetting ruleSetting);
    void delete(@Param("roomId") Long roomId);
}
