package com.saus.bgt.service.game;

import com.saus.bgt.generated.types.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<GameEntity, String> {
    List<Game> findAllByName(String name);
}
