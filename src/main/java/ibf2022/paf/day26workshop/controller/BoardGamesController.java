package ibf2022.paf.day26workshop.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.paf.day26workshop.model.Game;
import ibf2022.paf.day26workshop.model.Games;
import ibf2022.paf.day26workshop.repository.BoardGamesRespository;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@RestController
public class BoardGamesController {
    
    BoardGamesRespository boardGamesRespository;

    BoardGamesController(BoardGamesRespository boardGamesRespository) {
        this.boardGamesRespository = boardGamesRespository;
    }
    
    @GetMapping(path = "/games")
    public ResponseEntity<String> getAllBoardGames(@RequestParam Integer limit, @RequestParam Integer offset) {
        List<Game> listGames = boardGamesRespository.getAllGames(limit, offset);
        Games games = new Games();
        games.setGameList(listGames);
        games.setOffset(offset);
        games.setLimit(limit);
        games.setTotal(listGames.size());
        games.setTimestamp(LocalDate.now());

        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("boardgames", games.toJson());
        JsonObject result = objectBuilder.build();
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result.toString());
    }


    @GetMapping(path = "/games/rank")
    public ResponseEntity<String> getSortedBoardGames(@RequestParam Integer limit, @RequestParam Integer offset) {
        List<Game> listGames = boardGamesRespository.getSortedBoardGames(limit, offset);
        Games games = new Games();
        games.setGameList(listGames);
        games.setLimit(limit);
        games.setOffset(offset);
        games.setTotal(limit);
        games.setTimestamp(LocalDate.now());
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("boardgames", games.toJson());
        JsonObject result = objectBuilder.build();
        
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result.toString());
    }


    @GetMapping(path = "/games/{gameId}")
    public ResponseEntity<String> getBoardGameById(@PathVariable Integer gameId) {

        Game game = boardGamesRespository.getBoardGameById(gameId);

        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("game", game.toJson());

        JsonObject result = objectBuilder.build();

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(result.toString());
    }
}
