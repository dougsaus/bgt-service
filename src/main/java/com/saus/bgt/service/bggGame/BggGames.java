package com.saus.bgt.service.bggGame;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.saus.bgt.generated.types.Game;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BggGames {
    @JacksonXmlProperty(localName = "item")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<BggGame> games;
}