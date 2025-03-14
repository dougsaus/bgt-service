package com.saus.bgt.service.bggGame;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.saus.bgt.generated.types.GameMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BggClientImpl implements BggClient {
    private final WebClient.Builder webClientBuilder;

    public Mono<BggGame> fetchItem(Integer id) {
        // Make the WebClient call to get the XML response as a String
        String url = "https://boardgamegeek.com/xmlapi2/thing?id=" + id;

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
        return fetchItem(id)
                .map(bggGame -> GameMetadata.newBuilder()
                        .description(bggGame.getDescription())
                        .build())
                .block();
    }
}
