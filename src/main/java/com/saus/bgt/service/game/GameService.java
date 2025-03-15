package com.saus.bgt.service.game;

import com.saus.bgt.generated.types.Game;
import com.saus.bgt.generated.types.GameInput;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface GameService {
    Page<Game> findGames(PageRequest pageRequest);

    Game createGame(GameInput input);
}
