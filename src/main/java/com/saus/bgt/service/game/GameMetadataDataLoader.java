package com.saus.bgt.service.game;

import com.netflix.graphql.dgs.DgsDataLoader;
import com.saus.bgt.generated.types.GameMetadata;
import com.saus.bgt.service.bgg.BggClient;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import org.dataloader.MappedBatchLoader;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "metadata")
@RequiredArgsConstructor
public class GameMetadataDataLoader implements MappedBatchLoader<Integer, GameMetadata> {
    private final BggClient bggClient;

    @Override
    public CompletionStage<Map<Integer,GameMetadata>> load(Set<Integer> keys) {
        return CompletableFuture.supplyAsync(() -> bggClient.loadMetadata(keys));
    }
}