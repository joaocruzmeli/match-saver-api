package br.com.meli.matchsaver.controller;

import br.com.meli.matchsaver.model.MatchModel;
import br.com.meli.matchsaver.model.dto.MatchDto;
import br.com.meli.matchsaver.service.MatchService;
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

    @GetMapping(path = "{id}")
    ResponseEntity<MatchDto> getById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getById(id));
    }

    @PostMapping
    ResponseEntity<MatchModel> save(@RequestBody MatchDto matchDto){
        MatchModel newMatch = matchService.save(matchDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMatch);
    }

    @PutMapping(path = "{id}")
    ResponseEntity<MatchDto> update(@PathVariable Long id, @RequestBody MatchDto matchDto){
        MatchDto updatedMatch = matchService.update(id, matchDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMatch);
    }

    @DeleteMapping(path = "{id}")
    ResponseEntity<String>delete(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(matchService.delete(id));
    }
}
