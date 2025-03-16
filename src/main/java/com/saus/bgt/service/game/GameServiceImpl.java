package com.saus.bgt.service.game;

import com.saus.bgt.generated.types.Game;
import com.saus.bgt.generated.types.GameInput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    @Override
    public Page<Game> findGames(PageRequest pageRequest) {
        return gameRepository.findAll(pageRequest)
                .map(GameEntity::toGame);
    }

    @Override
    public Game createGame(GameInput input) {
        return gameRepository.save(GameEntity.builder()
                .id(UUID.randomUUID())
                .bggId(input.getBggId())
                .name(input.getName())
                .build()).toGame();
    }
}
