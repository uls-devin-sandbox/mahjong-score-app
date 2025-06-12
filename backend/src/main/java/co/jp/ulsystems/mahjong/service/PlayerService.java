package co.jp.ulsystems.mahjong.service;

import co.jp.ulsystems.mahjong.dto.PlayerDto;
import co.jp.ulsystems.mahjong.entity.Player;
import co.jp.ulsystems.mahjong.mapper.PlayerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService {
    
    private final PlayerMapper playerMapper;
    
    public List<PlayerDto> findAll() {
        List<Player> players = playerMapper.findAll();
        return players.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public PlayerDto findById(Long id) {
        Player player = playerMapper.findById(id);
        if (player == null) {
            throw new RuntimeException("プレイヤーが見つかりません: " + id);
        }
        return convertToDto(player);
    }
    
    public PlayerDto create(PlayerDto playerDto) {
        Player player = new Player();
        player.setName(playerDto.getName());
        player.setNickname(playerDto.getNickname());
        
        playerMapper.insert(player);
        return convertToDto(player);
    }
    
    private PlayerDto convertToDto(Player player) {
        PlayerDto dto = new PlayerDto();
        dto.setId(player.getId());
        dto.setName(player.getName());
        dto.setNickname(player.getNickname());
        dto.setCreatedAt(player.getCreatedAt());
        return dto;
    }
}
