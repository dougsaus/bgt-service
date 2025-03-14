package com.saus.bgt.service.bggGame;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BggGame {
    @JacksonXmlProperty(localName = "description")
    private String description;
}
