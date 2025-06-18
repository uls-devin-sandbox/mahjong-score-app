package co.jp.ulsystems.mahjong.mapper;

import co.jp.ulsystems.mahjong.entity.Game;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface GameMapper {
    List<Game> findByRoomId(@Param("roomId") Long roomId);
    Game findById(@Param("id") Long id);
    void insert(Game game);
    void update(Game game);
    void delete(@Param("id") Long id);
}
