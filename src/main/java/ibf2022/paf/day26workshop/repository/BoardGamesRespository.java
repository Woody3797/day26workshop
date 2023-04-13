package ibf2022.paf.day26workshop.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import ibf2022.paf.day26workshop.model.Game;

@Repository
public class BoardGamesRespository {
    
    @Autowired
    MongoTemplate template;

    public List<Game> getAllGames(Integer limit, Integer offset) {
        // Query query = new Query();
        // Pageable pageable = PageRequest.of(offset, limit);
        // query.with(pageable);
        Query query = Query.query(Criteria.where("gid").exists(true)).limit(limit).skip(offset);

        return template.find(query, Document.class, "games").stream().map(d -> Game.create(d)).toList();
    }

    public List<Game> getSortedBoardGames(Integer limit, Integer offset) {
        // Query query = new Query();
        // Pageable pageable = PageRequest.of(offset, limit);
        // query.with(pageable);
        Query query = Query.query(Criteria.where("gid").exists(true)).limit(limit).skip(offset);
        query.with(Sort.by(Sort.Direction.ASC, "ranking"));

        return template.find(query, Document.class, "games").stream().map(d -> Game.create(d)).toList();
    }

    public Game getBoardGameById(Integer gameId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("gid").is(gameId));
        
        return template.findOne(query, Game.class, "games");
    }
}
