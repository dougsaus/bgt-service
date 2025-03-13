package com.saus.bgt.service.game;


import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.saus.bgt.generated.types.Game;

import java.util.List;

@DgsComponent
public class GamesDataFetcher {
    /**
     * This dataFetcher resolves the games field on Query.
     */
    @DgsQuery
    public List<Game> games(@InputArgument("name") String name) {
        return List.of();
    }
}