package com.saus.bgt.service.bgg;

import com.fasterxml.jackson.databind.DeserializationFeature;
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
        String url = String.format("%s/thing?id=%s", config.getBgg().getBaseUrl(), buildIdsParameterValue(ids));

        return webClientBuilder
                .baseUrl(url)
                .build()
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        XmlMapper xmlMapper = new XmlMapper();
                        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        xmlMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);                        BggGames bggGames = xmlMapper.readValue(sanitizeXml(response), BggGames.class);
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

    private String sanitizeXml(String xml) {
        return xml.replaceAll("&(?!amp;|lt;|gt;|quot;|apos;)", "&amp;");
    }
}
