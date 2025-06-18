package co.jp.ulsystems.mahjong.service;

import co.jp.ulsystems.mahjong.dto.RoomDto;
import co.jp.ulsystems.mahjong.dto.PlayerDto;
import co.jp.ulsystems.mahjong.dto.RuleSettingDto;
import co.jp.ulsystems.mahjong.entity.Room;
import co.jp.ulsystems.mahjong.entity.Player;
import co.jp.ulsystems.mahjong.entity.RuleSetting;
import co.jp.ulsystems.mahjong.mapper.RoomMapper;
import co.jp.ulsystems.mahjong.mapper.PlayerMapper;
import co.jp.ulsystems.mahjong.mapper.RuleSettingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    
    private final RoomMapper roomMapper;
    private final PlayerMapper playerMapper;
    private final RuleSettingMapper ruleSettingMapper;
    
    public List<RoomDto> findAll() {
        List<Room> rooms = roomMapper.findAll();
        return rooms.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public RoomDto findById(Long id) {
        Room room = roomMapper.findById(id);
        if (room == null) {
            throw new RuntimeException("部屋が見つかりません: " + id);
        }
        return convertToDto(room);
    }
    
    @Transactional
    public RoomDto create(RoomDto roomDto) {
        Room room = new Room();
        room.setName(roomDto.getName());
        room.setYearMonth(roomDto.getYearMonth());
        
        roomMapper.insert(room);
        
        RuleSetting defaultRule = new RuleSetting();
        defaultRule.setRoomId(room.getId());
        defaultRule.setUma1st(20000);
        defaultRule.setUma2nd(10000);
        defaultRule.setUma3rd(-10000);
        defaultRule.setUma4th(-20000);
        defaultRule.setOka(25000);
        defaultRule.setTobiPenalty(-30000);
        ruleSettingMapper.insert(defaultRule);
        
        return convertToDto(room);
    }
    
    @Transactional
    public void addPlayerToRoom(Long roomId, Long playerId) {
        playerMapper.addPlayerToRoom(roomId, playerId);
    }
    
    private RoomDto convertToDto(Room room) {
        RoomDto dto = new RoomDto();
        dto.setId(room.getId());
        dto.setName(room.getName());
        dto.setYearMonth(room.getYearMonth());
        dto.setCreatedAt(room.getCreatedAt());
        
        List<Player> players = playerMapper.findByRoomId(room.getId());
        dto.setPlayers(players.stream()
                .map(this::convertPlayerToDto)
                .collect(Collectors.toList()));
        
        RuleSetting ruleSetting = ruleSettingMapper.findByRoomId(room.getId());
        if (ruleSetting != null) {
            dto.setRuleSetting(convertRuleSettingToDto(ruleSetting));
        }
        
        return dto;
    }
    
    private PlayerDto convertPlayerToDto(Player player) {
        PlayerDto dto = new PlayerDto();
        dto.setId(player.getId());
        dto.setName(player.getName());
        dto.setNickname(player.getNickname());
        dto.setCreatedAt(player.getCreatedAt());
        return dto;
    }
    
    private RuleSettingDto convertRuleSettingToDto(RuleSetting ruleSetting) {
        RuleSettingDto dto = new RuleSettingDto();
        dto.setId(ruleSetting.getId());
        dto.setRoomId(ruleSetting.getRoomId());
        dto.setUma1st(ruleSetting.getUma1st());
        dto.setUma2nd(ruleSetting.getUma2nd());
        dto.setUma3rd(ruleSetting.getUma3rd());
        dto.setUma4th(ruleSetting.getUma4th());
        dto.setOka(ruleSetting.getOka());
        dto.setTobiPenalty(ruleSetting.getTobiPenalty());
        return dto;
    }
}
