package com.saus.bgt.service.bgg;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.saus.bgt.generated.types.GameMetadata;
import com.saus.bgt.service.config.BgtServiceConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BggClientImpl implements BggClient {
    private final WebClient.Builder webClientBuilder;
    private final BgtServiceConfiguration config;

    public List<BggGame> fetchItems(Set<Integer> ids) {
        String url = String.format("%s/thing?id=%s", config.getBgg().getBaseUrl(), buildIdsParameterValue(ids));

        BggGames games = webClientBuilder
                .baseUrl(url)
                .build()
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        XmlMapper xmlMapper = new XmlMapper();
                        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        xmlMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                        return xmlMapper.readValue(sanitizeXml(response), BggGames.class);
                    } catch (Exception e) {
                        return BggGames.builder().build();
                    }
                })
                .block();
        return games != null ? games.getGames() : List.of();
    }

    @Override
    public Map<Integer, GameMetadata> loadMetadata(Set<Integer> ids) {
        Map<Integer, GameMetadata> fetchedMetadata = fetchItems(ids)
                .stream()
                .collect(Collectors.toMap(
                        BggGame::getId,
                        bggGame -> GameMetadata.newBuilder()
                                .description(bggGame.getDescription())
                                .build()
                ));

        ids.stream()
                .filter(id -> !fetchedMetadata.containsKey(id))
                .forEach(id -> fetchedMetadata.put(id, GameMetadata.newBuilder().build()));

        return fetchedMetadata;
    }

    private String buildIdsParameterValue(Set<Integer> ids) {
        return ids.stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    private String sanitizeXml(String xml) {
        return xml.replaceAll("&(?!amp;|lt;|gt;|quot;|apos;)", "&amp;");
    }
}
