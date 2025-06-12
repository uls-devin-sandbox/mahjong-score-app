package co.jp.ulsystems.mahjong.controller;

import co.jp.ulsystems.mahjong.dto.RuleSettingDto;
import co.jp.ulsystems.mahjong.service.RuleSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RuleSettingController {
    
    private final RuleSettingService ruleSettingService;
    
    @GetMapping("/room/{roomId}")
    public ResponseEntity<RuleSettingDto> getRuleByRoomId(@PathVariable Long roomId) {
        RuleSettingDto ruleSetting = ruleSettingService.findByRoomId(roomId);
        return ResponseEntity.ok(ruleSetting);
    }
    
    @PostMapping
    public ResponseEntity<RuleSettingDto> createOrUpdateRule(@RequestBody RuleSettingDto ruleSettingDto) {
        RuleSettingDto savedRule = ruleSettingService.createOrUpdate(ruleSettingDto);
        return ResponseEntity.ok(savedRule);
    }
}
