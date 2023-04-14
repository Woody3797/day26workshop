package ibf2022.paf.day26workshop.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.paf.day26workshop.model.Game;
import ibf2022.paf.day26workshop.model.Games;
import ibf2022.paf.day26workshop.repository.BoardGamesRespository;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
public class BoardGamesController {
    
    BoardGamesRespository boardGamesRespository;

    BoardGamesController(BoardGamesRespository boardGamesRespository) {
        this.boardGamesRespository = boardGamesRespository;
    }
    
    @GetMapping(path = "/games")
    public ResponseEntity<String> getAllBoardGames(@RequestParam(defaultValue = "25") Integer limit, @RequestParam(defaultValue = "0") Integer offset) {
        List<Game> listGames = boardGamesRespository.getAllGames(limit, offset);
        Games games = new Games();
        games.setGameList(listGames);
        games.setOffset(offset);
        games.setLimit(limit);
        games.setTotal(listGames.size());
        games.setTimestamp(LocalDate.now());

        JsonObject result = Json.createObjectBuilder()
        .add("boardgames", games.toJson())
        .build();
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result.toString());
    }

    @GetMapping(path = "/games/rank")
    public ResponseEntity<String> getSortedBoardGames(@RequestParam(defaultValue = "25") Integer limit, @RequestParam(defaultValue = "0") Integer offset) {
        List<Game> listGames = boardGamesRespository.getSortedBoardGames(limit, offset);
        Games games = new Games();
        games.setGameList(listGames);
        games.setLimit(limit);
        games.setOffset(offset);
        games.setTotal(limit);
        games.setTimestamp(LocalDate.now());
        JsonObject result = Json.createObjectBuilder()
        .add("boardgames", games.toJson())
        .build();
        
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result.toString());
    }

    @GetMapping(path = "/games/{gameId}")
    public ResponseEntity<String> getBoardGameById(@PathVariable String gameId) {
        Game game = boardGamesRespository.getBoardGameById(gameId);
        if (game == null) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"error\": \"game " + gameId + " not found\"}");
        }
        JsonObject result = Json.createObjectBuilder()
        .add("game", game.toJson())
        .build();

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result.toString());
    }

    @GetMapping(path = "/game/{gameId}")
    @ResponseBody
    public String getGameToJson(@PathVariable String gameId) {
        String game = boardGamesRespository.getGameToJson(gameId);
        if (game == null) {
            return Json.createObjectBuilder()
            .add("error", "game " + gameId + " not found")
            .build().toString();
        }
        return game;
    }

}
