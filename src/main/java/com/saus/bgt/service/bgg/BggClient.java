package com.saus.bgt.service.bgg;

import com.saus.bgt.generated.types.GameMetadata;

public interface BggClient {
    GameMetadata lookupGame(Integer id);
}
