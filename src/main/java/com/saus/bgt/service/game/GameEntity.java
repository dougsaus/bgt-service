package com.saus.bgt.service.game;

import com.saus.bgt.generated.types.Game;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity(name = "games")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GameEntity {
    @Id
    private Integer id;
    private String name;

    public Game toGame() {
        return Game.newBuilder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}