package com.saus.bgt.service.game;

import com.saus.bgt.generated.types.Game;
import com.saus.bgt.generated.types.GameInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
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

    @Override
    public Game createGame(GameInput input) {
        return gameRepository.save(GameEntity.builder()
                .id(UUID.randomUUID())
                .name(input.getName())
                .bggLink(input.getBggLink() != null ? input.getBggLink() : "")
                .build()).toGame();
    }
}
