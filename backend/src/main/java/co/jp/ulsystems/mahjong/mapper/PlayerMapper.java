package co.jp.ulsystems.mahjong.mapper;

import co.jp.ulsystems.mahjong.entity.Player;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PlayerMapper {
    List<Player> findAll();
    Player findById(@Param("id") Long id);
    List<Player> findByRoomId(@Param("roomId") Long roomId);
    void insert(Player player);
    void update(Player player);
    void delete(@Param("id") Long id);
    void addPlayerToRoom(@Param("roomId") Long roomId, @Param("playerId") Long playerId);
    void removePlayerFromRoom(@Param("roomId") Long roomId, @Param("playerId") Long playerId);
}
