package com.saus.bgt.service.game;

import com.saus.bgt.generated.types.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    @Override
    public List<Game> findAllGames() {
        return gameRepository.findAll()
                .stream()
                .map(GameEntity::toGame)
                .collect(Collectors.toList());
    }
}
