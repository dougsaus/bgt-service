package com.saus.bgt.service.game;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameRepository extends JpaRepository<GameEntity, Integer> {
}
