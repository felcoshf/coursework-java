package com.example.game_library.repository;

import com.example.game_library.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findByUserId(Long userId);
    List<Game> findByUserIdAndPlatform(Long userId, String platform);
}
