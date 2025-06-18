package co.jp.ulsystems.mahjong.mapper;

import co.jp.ulsystems.mahjong.entity.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RoomMapper {
    List<Room> findAll();
    Room findById(@Param("id") Long id);
    void insert(Room room);
    void update(Room room);
    void delete(@Param("id") Long id);
}
