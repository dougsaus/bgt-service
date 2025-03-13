package com.saus.bgt.service.game;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.test.EnableDgsTest;
import com.saus.bgt.generated.types.Game;
import com.saus.bgt.service.NameGeneratingTest;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@SpringBootTest(classes = {GamesDataFetcher.class})
@EnableDgsTest
class ShowsDataFetcherTest extends NameGeneratingTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

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
}