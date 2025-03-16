package com.saus.bgt.service.bgg;

import com.saus.bgt.generated.types.GameMetadata;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BggClient {
    Map<Integer, GameMetadata> loadMetadata(Set<Integer> ids);
}
