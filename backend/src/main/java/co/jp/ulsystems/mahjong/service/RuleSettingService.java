package co.jp.ulsystems.mahjong.service;

import co.jp.ulsystems.mahjong.dto.RuleSettingDto;
import co.jp.ulsystems.mahjong.entity.RuleSetting;
import co.jp.ulsystems.mahjong.mapper.RuleSettingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RuleSettingService {
    
    private final RuleSettingMapper ruleSettingMapper;
    
    public RuleSettingDto findByRoomId(Long roomId) {
        RuleSetting ruleSetting = ruleSettingMapper.findByRoomId(roomId);
        if (ruleSetting == null) {
            return null;
        }
        return convertToDto(ruleSetting);
    }
    
    public RuleSettingDto createOrUpdate(RuleSettingDto dto) {
        RuleSetting existing = ruleSettingMapper.findByRoomId(dto.getRoomId());
        
        RuleSetting ruleSetting = new RuleSetting();
        ruleSetting.setRoomId(dto.getRoomId());
        ruleSetting.setUma1st(dto.getUma1st());
        ruleSetting.setUma2nd(dto.getUma2nd());
        ruleSetting.setUma3rd(dto.getUma3rd());
        ruleSetting.setUma4th(dto.getUma4th());
        ruleSetting.setOka(dto.getOka());
        ruleSetting.setTobiPenalty(dto.getTobiPenalty());
        
        if (existing == null) {
            ruleSettingMapper.insert(ruleSetting);
        } else {
            ruleSettingMapper.update(ruleSetting);
        }
        
        return convertToDto(ruleSetting);
    }
    
    private RuleSettingDto convertToDto(RuleSetting ruleSetting) {
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
