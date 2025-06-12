package co.jp.ulsystems.mahjong.service;

import co.jp.ulsystems.mahjong.dto.CreateGameRequest;
import co.jp.ulsystems.mahjong.dto.GameDto;
import co.jp.ulsystems.mahjong.dto.GameResultDto;
import co.jp.ulsystems.mahjong.entity.Game;
import co.jp.ulsystems.mahjong.entity.GameResult;
import co.jp.ulsystems.mahjong.entity.Player;
import co.jp.ulsystems.mahjong.entity.RuleSetting;
import co.jp.ulsystems.mahjong.mapper.GameMapper;
import co.jp.ulsystems.mahjong.mapper.GameResultMapper;
import co.jp.ulsystems.mahjong.mapper.PlayerMapper;
import co.jp.ulsystems.mahjong.mapper.RuleSettingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class GameService {
    
    private final GameMapper gameMapper;
    private final GameResultMapper gameResultMapper;
    private final PlayerMapper playerMapper;
    private final RuleSettingMapper ruleSettingMapper;
    
    public List<GameDto> findByRoomId(Long roomId) {
        List<Game> games = gameMapper.findByRoomId(roomId);
        return games.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<GameResultDto> findByPlayerId(Long playerId) {
        List<GameResult> results = gameResultMapper.findByPlayerId(playerId);
        return results.stream()
                .map(this::convertResultToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public GameDto create(CreateGameRequest request) {
        Game game = new Game();
        game.setRoomId(request.getRoomId());
        game.setGameDate(request.getGameDate());
        game.setStartingPlayerId(request.getStartingPlayerId());
        
        gameMapper.insert(game);
        
        RuleSetting ruleSetting = ruleSettingMapper.findByRoomId(request.getRoomId());
        if (ruleSetting == null) {
            throw new RuntimeException("ルール設定が見つかりません");
        }
        
        List<CreateGameRequest.GameResultRequest> sortedResults = request.getResults().stream()
                .sorted(Comparator.comparing(CreateGameRequest.GameResultRequest::getRawScore).reversed())
                .collect(Collectors.toList());
        
        for (int i = 0; i < sortedResults.size(); i++) {
            CreateGameRequest.GameResultRequest resultRequest = sortedResults.get(i);
            
            GameResult gameResult = new GameResult();
            gameResult.setGameId(game.getId());
            gameResult.setPlayerId(resultRequest.getPlayerId());
            gameResult.setRawScore(resultRequest.getRawScore());
            gameResult.setRank(i + 1);
            gameResult.setIsTobi(resultRequest.getIsTobi());
            
            int finalScore = calculateFinalScore(resultRequest.getRawScore(), i + 1, 
                    resultRequest.getIsTobi(), ruleSetting);
            gameResult.setFinalScore(finalScore);
            
            gameResultMapper.insert(gameResult);
        }
        
        return convertToDto(game);
    }
    
    private int calculateFinalScore(int rawScore, int rank, boolean isTobi, RuleSetting ruleSetting) {
        int finalScore = (rawScore - ruleSetting.getOka()) / 1000;
        
        switch (rank) {
            case 1:
                finalScore += ruleSetting.getUma1st() / 1000;
                break;
            case 2:
                finalScore += ruleSetting.getUma2nd() / 1000;
                break;
            case 3:
                finalScore += ruleSetting.getUma3rd() / 1000;
                break;
            case 4:
                finalScore += ruleSetting.getUma4th() / 1000;
                break;
        }
        
        if (isTobi) {
            finalScore += ruleSetting.getTobiPenalty() / 1000;
        }
        
        return finalScore * 1000;
    }
    
    private GameDto convertToDto(Game game) {
        GameDto dto = new GameDto();
        dto.setId(game.getId());
        dto.setRoomId(game.getRoomId());
        dto.setGameDate(game.getGameDate());
        dto.setStartingPlayerId(game.getStartingPlayerId());
        
        Player startingPlayer = playerMapper.findById(game.getStartingPlayerId());
        if (startingPlayer != null) {
            dto.setStartingPlayerName(startingPlayer.getName());
        }
        
        List<GameResult> results = gameResultMapper.findByGameId(game.getId());
        dto.setResults(results.stream()
                .map(this::convertResultToDto)
                .collect(Collectors.toList()));
        
        return dto;
    }
    
    private GameResultDto convertResultToDto(GameResult result) {
        GameResultDto dto = new GameResultDto();
        dto.setId(result.getId());
        dto.setGameId(result.getGameId());
        dto.setPlayerId(result.getPlayerId());
        dto.setRawScore(result.getRawScore());
        dto.setRank(result.getRank());
        dto.setIsTobi(result.getIsTobi());
        dto.setFinalScore(result.getFinalScore());
        
        Player player = playerMapper.findById(result.getPlayerId());
        if (player != null) {
            dto.setPlayerName(player.getName());
        }
        
        return dto;
    }
}
