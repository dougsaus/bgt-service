package com.saus.bgt.service.bggGame;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.saus.bgt.generated.types.GameMetadata;
import com.saus.bgt.service.config.BgtServiceConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BggClientImpl implements BggClient {
    private final WebClient.Builder webClientBuilder;
    private final BgtServiceConfiguration config;

    public Mono<BggGame> fetchItems(List<Integer> ids) {
        String url = String.format("%s/thing/id=%s", config.getBgg().getBaseUrl(), buildIdsParameterValue(ids));

        return webClientBuilder
                .baseUrl(url)
                .build()
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        XmlMapper xmlMapper = new XmlMapper();
                        BggGames bggGames = xmlMapper.readValue(response, BggGames.class);
                        return bggGames.getGames().getFirst();
                    } catch (Exception e) {
                        return BggGame.builder().build();
                    }
                });
    }

    @Override
    public GameMetadata lookupGame(Integer id) {
        return fetchItems(List.of(id))
                .map(bggGame -> GameMetadata.newBuilder()
                        .description(bggGame.getDescription())
                        .build())
                .block();
    }

    private String buildIdsParameterValue(List<Integer> ids) {
        return ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
