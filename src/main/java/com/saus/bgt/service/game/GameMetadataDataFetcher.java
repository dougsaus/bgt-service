package com.saus.bgt.service.game;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.saus.bgt.generated.types.Game;
import com.saus.bgt.generated.types.GameMetadata;
import com.saus.bgt.service.bggGame.BggClient;
import lombok.RequiredArgsConstructor;

@DgsComponent
@RequiredArgsConstructor
public class GameMetadataDataFetcher {
    private final BggClient bggClient;

    @DgsData(parentType = "Game")
    public GameMetadata metadata(DgsDataFetchingEnvironment dfe) {
        Game game = dfe.getSource();
        assert game != null;
        return bggClient.lookupGame(game.getBggId());
    }
}