package com.saus.bgt.service.game;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.test.EnableDgsTest;
import com.saus.bgt.generated.types.Game;
import com.saus.bgt.service.NameGeneratingTest;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;


@Testcontainers
@SpringBootTest
@EnableDgsTest
class GamesDataFetcherTest extends NameGeneratingTest {

    private static final DockerImageName postgresImage = DockerImageName.parse("postgres:14.3")
            .asCompatibleSubstituteFor("postgres");

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            postgresImage
    );

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    void given_empty_database__when_query_for_all_games__then_return_empty_list_of_games() {

        @Language("GraphQL") String query = """
                query {
                    games {
                        id
                        name
                        bggLink
                    }
                }
                """;

        List<Game> games = dgsQueryExecutor.executeAndExtractJsonPath(query, "data.games[*]");
        assertThat(games).isEmpty();
    }

    @Test
    @Sql(scripts = "/scenarios/game/query-all-games/given.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/scenarios/default/clear-db.sql", executionPhase = AFTER_TEST_METHOD)
    void given_multiple_records_in_database__when_query_for_all_games__then_return_list_of_games() {

        @Language("GraphQL") String query = """
                query {
                    games {
                        id
                        name
                        bggLink
                    }
                }
                """;

        List<Game> games = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query, "data.games[*]", new TypeRef<>() {
        });

        assertThat(games).isNotEmpty();
        Game game = games.getFirst();
        assertThat(game.getId()).isEqualTo("a9c4955e-bf92-418f-b2bf-0421683f4001");
        assertThat(game.getName()).isEqualTo("Game1");
        assertThat(game.getBggLink()).isEqualTo("Link1");

        game = games.getLast();
        assertThat(game.getId()).isEqualTo("a9c4955e-bf92-418f-b2bf-0421683f4002");
        assertThat(game.getName()).isEqualTo("Game2");
        assertThat(game.getBggLink()).isEqualTo("");
    }
}