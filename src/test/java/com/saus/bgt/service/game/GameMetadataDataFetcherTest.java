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
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.verify.VerificationTimes;
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

import static com.saus.bgt.service.MockServerPortHelper.getAvailablePortForTest;
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
        mockServerPort = getAvailablePortForTest(GameMetadataDataFetcherTest.class.getSimpleName());
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
                                .withMethod(HttpMethod.GET.toString())
                                .withPath("/thing")
                                .withQueryStringParameter("id", "1,2,4"),
                        Times.exactly(1))
                .respond(response()
                        .withStatusCode(HttpStatusCode.OK_200.code())
                        .withHeader("Content-Type", "application/xml")
                        .withBody(readFileFromTestResources("scenarios/game/query-with-description/bgg-games-response-all.xml")));

        @Language("GraphQL") String query = """
                query {
                    queryGames {
                        games{
                            id
                            bggId
                            name
                            metadata {
                                description
                            }
                        }
                    }
                }
                """;

        List<Game> games = dgsQueryExecutor.executeAndExtractJsonPathAsObject(query, "data.queryGames.games[*]", new TypeRef<>() {
        });

        assertThat(games).isNotEmpty();
        Game game = games.getFirst();
        assertThat(game.getId()).isEqualTo("fa118ba3-00b4-4266-a17e-ed1c3aa4fa01");
        assertThat(game.getBggId()).isEqualTo(1);
        assertThat(game.getName()).isEqualTo("Game1");
        assertThat(game.getMetadata().getDescription()).isEqualTo("Description 1");

        game = games.get(1);
        assertThat(game.getId()).isEqualTo("fa118ba3-00b4-4266-a17e-ed1c3aa4fa02");
        assertThat(game.getBggId()).isEqualTo(2);
        assertThat(game.getName()).isEqualTo("Game2");
        assertThat(game.getMetadata().getDescription()).isEqualTo("Description 2");

        game = games.get(2);
        assertThat(game.getId()).isEqualTo("fa118ba3-00b4-4266-a17e-ed1c3aa4fa03");
        assertThat(game.getBggId()).isNull();
        assertThat(game.getName()).isEqualTo("Game3");
        assertThat(game.getMetadata()).isNotNull();
        assertThat(game.getMetadata().getDescription()).isNull();

        game = games.get(3);
        assertThat(game.getId()).isEqualTo("fa118ba3-00b4-4266-a17e-ed1c3aa4fa04");
        assertThat(game.getBggId()).isEqualTo(4);
        assertThat(game.getName()).isEqualTo("Game4");
        assertThat(game.getMetadata().getDescription()).isEqualTo("Description 4");

        server.verify(
                request()
                        .withMethod(HttpMethod.GET.toString())
                        .withPath("/thing")
                        .withQueryStringParameter("id", "1,2,4"),
                VerificationTimes.exactly(1)
        );

        HttpRequest[] httpRequests = server.retrieveRecordedRequests(null);
        assertThat(httpRequests).hasSize(1);
    }
}