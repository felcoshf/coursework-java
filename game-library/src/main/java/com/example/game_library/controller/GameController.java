package com.example.game_library.controller;

import com.example.game_library.model.Game;
import com.example.game_library.model.User;
import com.example.game_library.service.GameService;
import com.example.game_library.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;
    private final UserService userService;

    private User currentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute(AuthController.SESSION_USER_ID);
        if (userId == null) return null;
        return userService.findById(userId);
    }

    private boolean notLoggedIn(HttpSession session) {
        return session.getAttribute(AuthController.SESSION_USER_ID) == null;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String platform,
                       HttpSession session,
                       Model model) {
        if (notLoggedIn(session)) return "redirect:/login";
        User user = currentUser(session);

        model.addAttribute("games", gameService.getUserGames(user.getId(), platform));
        model.addAttribute("platforms", GameService.PLATFORMS);
        model.addAttribute("selectedPlatform", platform);
        model.addAttribute("login", user.getLogin());
        return "games";
    }

    @GetMapping("/add")
    public String addForm(HttpSession session, Model model) {
        if (notLoggedIn(session)) return "redirect:/login";
        model.addAttribute("platforms", GameService.PLATFORMS);
        model.addAttribute("statuses", GameService.STATUSES);
        return "game-form";
    }

    @PostMapping("/add")
    public String add(@RequestParam String title,
                      @RequestParam String platform,
                      @RequestParam String status,
                      HttpSession session) {
        if (notLoggedIn(session)) return "redirect:/login";
        User user = currentUser(session);
        gameService.save(new Game(user.getId(), title, platform, status));
        return "redirect:/games";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id,
                           HttpSession session,
                           Model model) {
        if (notLoggedIn(session)) return "redirect:/login";
        User user = currentUser(session);
        Game game = gameService.getById(id);
        if (game == null || !game.getUserId().equals(user.getId())) {
            return "redirect:/games";
        }
        model.addAttribute("game", game);
        model.addAttribute("platforms", GameService.PLATFORMS);
        model.addAttribute("statuses", GameService.STATUSES);
        return "game-edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @RequestParam String title,
                       @RequestParam String platform,
                       @RequestParam String status,
                       HttpSession session) {
        if (notLoggedIn(session)) return "redirect:/login";
        User user = currentUser(session);
        Game game = gameService.getById(id);
        if (game != null && game.getUserId().equals(user.getId())) {
            game.setTitle(title);
            game.setPlatform(platform);
            game.setStatus(status);
            gameService.save(game);
        }
        return "redirect:/games";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        if (notLoggedIn(session)) return "redirect:/login";
        User user = currentUser(session);
        Game game = gameService.getById(id);
        if (game != null && game.getUserId().equals(user.getId())) {
            gameService.delete(id);
        }
        return "redirect:/games";
    }
}
