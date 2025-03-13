package com.saus.bgt.service.game;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.saus.bgt.generated.types.Game;
import com.saus.bgt.generated.types.GameInput;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class GamesDataFetcher {
    private final GameService gameService;

    /**
     * This dataFetcher resolves the games field on Query.
     */
    @DgsQuery
    public List<Game> games() {
        return gameService.findAllGames();
    }

    @DgsMutation
    public Game createGame(@InputArgument GameInput input) {
        return gameService.createGame(input);
    }
}