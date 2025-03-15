package com.saus.bgt.service.game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface GameRepository extends JpaRepository<GameEntity, UUID>, PagingAndSortingRepository<GameEntity, UUID> {
}
