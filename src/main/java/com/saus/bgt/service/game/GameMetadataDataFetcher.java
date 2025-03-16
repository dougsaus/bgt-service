package com.saus.bgt.service.game;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.saus.bgt.generated.types.Game;
import com.saus.bgt.generated.types.GameMetadata;
import com.saus.bgt.service.bgg.BggClient;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;

import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class GameMetadataDataFetcher {
    private final BggClient bggClient;

    @DgsData(parentType = "Game")
    public CompletableFuture<GameMetadata> metadata(DgsDataFetchingEnvironment dfe) {
        Game game = dfe.getSource();
        assert game != null;
        DataLoader<Integer, GameMetadata> dataLoader = dfe.getDataLoader("metadata");

        assert dataLoader != null;
        return dataLoader.load(game.getBggId());
    }
}