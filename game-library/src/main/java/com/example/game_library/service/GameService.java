package com.example.game_library.service;

import com.example.game_library.model.Game;
import com.example.game_library.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    public static final List<String> PLATFORMS = List.of("PC", "PlayStation", "Xbox", "Nintendo");
    public static final List<String> STATUSES = List.of("Не начата", "Играю", "Пройдена");

    private final GameRepository gameRepository;

    public List<Game> getUserGames(Long userId, String platformFilter) {
        if (platformFilter != null && !platformFilter.isBlank()) {
            return gameRepository.findByUserIdAndPlatform(userId, platformFilter);
        }
        return gameRepository.findByUserId(userId);
    }

    public Game getById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public void delete(Long id) {
        gameRepository.deleteById(id);
    }
}
