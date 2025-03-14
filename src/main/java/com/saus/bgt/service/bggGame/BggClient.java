package com.saus.bgt.service.bggGame;

import com.saus.bgt.generated.types.GameMetadata;

public interface BggClient {
    GameMetadata lookupGame(Integer id);
}
