package br.com.meli.matchsaver.controller;

import br.com.meli.matchsaver.model.MatchModel;
import br.com.meli.matchsaver.model.dto.MatchDto;
import br.com.meli.matchsaver.service.MatchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matchs")
public class MatchController {
    @Autowired
    private MatchService matchService;

    @GetMapping
    ResponseEntity<List<MatchDto>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<MatchDto> getById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getById(id));
    }

    @GetMapping("/trashes")
    ResponseEntity<List<MatchModel>> getAllTrashes(){
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getAllTrashes());
    }

    @GetMapping("/goalless")
    ResponseEntity<List<MatchModel>> getAllGoalless(){
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getAllGoalless());
    }

    @GetMapping("/club")
    ResponseEntity<List<MatchModel>> getAllByClubName(@RequestParam String name, @RequestParam boolean isClubHome){
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getAllByClubName(name, isClubHome));
    }

    @GetMapping("/stadium")
    ResponseEntity<List<MatchModel>> getAllByStadium(@RequestParam String name){
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getAllByStadium(name));
    }

    @PostMapping
    ResponseEntity<MatchModel> save(@Valid @RequestBody MatchDto matchDto){
        MatchModel newMatch = matchService.save(matchDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMatch);
    }

    @PutMapping("/{id}")
    ResponseEntity<MatchDto> update(@PathVariable Long id, @Valid @RequestBody MatchDto matchDto){
        MatchDto updatedMatch = matchService.update(id, matchDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMatch);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String>delete(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(matchService.delete(id));
    }
}
