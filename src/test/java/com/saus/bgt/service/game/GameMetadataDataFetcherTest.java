package com.saus.bgt.service.game;

import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.test.EnableDgsTest;
import com.saus.bgt.generated.types.Game;
import com.saus.bgt.service.NameGeneratingTest;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.HttpStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static com.saus.bgt.service.TestHelper.readFileFromTestResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;


@Testcontainers
@SpringBootTest
@EnableDgsTest
class GameMetadataDataFetcherTest extends NameGeneratingTest {

    private static final DockerImageName postgresImage = DockerImageName.parse("postgres:14.3")
            .asCompatibleSubstituteFor("postgres");

    private static ClientAndServer server;

    private static int mockServerPort;

    @BeforeAll
    public static void beforeAll() {
        mockServerPort = 9999; // getAvailablePortForTest(GameMetadataDataFetcherTest.class.getSimpleName());
        server = startClientAndServer(mockServerPort);
    }

    @AfterAll
    public static void afterAll() {
        server.stop();
    }

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            postgresImage
    );

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("bgt.bgg.base-url", () -> String.format("http://localhost:%d", mockServerPort));
    }

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    @Sql(scripts = "/scenarios/game/query-with-description/given.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/scenarios/default/clear-db.sql", executionPhase = AFTER_TEST_METHOD)
    void given_request_for_metadata_description___when_query_for_all_games__then_return_list_of_games_with_metadata() {

        server.when(request()
                                .withMethod(HttpMethod.POST.toString())
                                .withPath("/thing")
                                .withPathParameter("id", "1"),
                        Times.exactly(1))
                .respond(response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withHeader("Content-Type", "application/xml")
                        .withBody(readFileFromTestResources("scenarios/game/query-with-description/bgg-games-response1.xml")));

        server.when(request()
                                .withMethod(HttpMethod.POST.toString())
                                .withPath("/thing")
                                .withPathParameter("id", "2"),
                        Times.exactly(1))
                .respond(response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withHeader("Content-Type", "application/xml")
                        .withBody(readFileFromTestResources("scenarios/game/query-with-description/bgg-games-response2.xml")));

        @Language("GraphQL") String query = """
                query {
                    queryGames {
                        games{
                            id
                            name
                            metadata {
                                description
                            }
                        }
                    }
                }
                """;

        List<Game> games = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query, "data.games[*]", new TypeRef<>() {
        });

        assertThat(games).isNotEmpty();
        Game game = games.getFirst();
        assertThat(game.getId()).isEqualTo(1);
        assertThat(game.getName()).isEqualTo("Game1");
        assertThat(game.getMetadata().getDescription()).isEqualTo("Game1 description");

        game = games.getLast();
        assertThat(game.getId()).isEqualTo(2);
        assertThat(game.getName()).isEqualTo("Game2");
        assertThat(game.getMetadata().getDescription()).isEqualTo("Game2 description");
    }
}