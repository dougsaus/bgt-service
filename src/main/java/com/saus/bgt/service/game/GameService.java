package com.saus.bgt.service.game;

import com.saus.bgt.generated.types.Game;

import java.util.List;

public interface GameService {
    List<Game> findAllGames();
}
