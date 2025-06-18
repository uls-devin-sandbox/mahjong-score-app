package co.jp.ulsystems.mahjong.mapper;

import co.jp.ulsystems.mahjong.entity.GameResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface GameResultMapper {
    List<GameResult> findByGameId(@Param("gameId") Long gameId);
    List<GameResult> findByPlayerId(@Param("playerId") Long playerId);
    List<GameResult> findByRoomId(@Param("roomId") Long roomId);
    void insert(GameResult gameResult);
    void update(GameResult gameResult);
    void delete(@Param("id") Long id);
    void deleteByGameId(@Param("gameId") Long gameId);
}
