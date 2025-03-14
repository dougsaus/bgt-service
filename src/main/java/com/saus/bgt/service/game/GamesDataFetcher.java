package com.saus.bgt.service.game;

import com.netflix.graphql.dgs.*;
import com.saus.bgt.generated.types.Game;
import com.saus.bgt.generated.types.GameInput;
import com.saus.bgt.service.util.CsvReader;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@DgsComponent
@RequiredArgsConstructor
public class GamesDataFetcher {
    private final GameService gameService;

    @DgsQuery
    public List<Game> games() {
        return gameService.findAllGames();
    }

    @DgsMutation
    public Game createGame(@InputArgument GameInput input) {
        return gameService.createGame(input);
    }

    @DgsMutation
    public List<Game> seedGames(@InputArgument String filename) {
        List<Map<String, String>> maps = CsvReader.readCsvFileAsMap(filename);
        return maps
                .stream()
                .map(gameRecord -> gameService.createGame(GameInput.newBuilder()
                        .id(Integer.parseInt(gameRecord.get("id")))
                        .name(gameRecord.get("name"))
                        .build()))
                .collect(Collectors.toList());
    }

}