package com.saus.bgt.service.game;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.saus.bgt.generated.types.Game;
import com.saus.bgt.generated.types.GameConnection;
import com.saus.bgt.generated.types.GameEdge;
import com.saus.bgt.generated.types.GameInput;
import com.saus.bgt.service.common.BgtDataFetcher;
import com.saus.bgt.service.common.CsvReader;
import com.saus.bgt.service.common.CursorHelper;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@DgsComponent
@RequiredArgsConstructor
public class GamesDataFetcher implements BgtDataFetcher {
    private final GameService gameService;
    private final CursorHelper cursorHelper;

    @DgsQuery
    public GameConnection queryGames(DataFetchingEnvironment dfe) {
        int pageNum = getOffsetFromDataFetchingEnvironment(dfe);

        Page<Game> games = gameService.findGames(createPageRequest(dfe, pageNum));
        return GameConnection.newBuilder()
                .pageInfo(createPageInfo(games, pageNum))
                .count(games.getContent().size())
                .totalCount((int) games.getTotalElements())
                .edges(games
                        .stream()
                        .map(game -> GameEdge.newBuilder()
                                .cursor("")
                                .node(game)
                                .build())
                        .collect(Collectors.toList()))
                .games(games.stream().toList())
                .build();
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
                        .bggId(Integer.parseInt(gameRecord.get("id")))
                        .name(gameRecord.get("name"))
                        .build()))
                .collect(Collectors.toList());
    }

    @Override
    public CursorHelper cursorHelper() {
        return cursorHelper;
    }
}