package br.com.meli.matchsaver.controller;

import br.com.meli.matchsaver.model.dto.MatchDto;
import br.com.meli.matchsaver.model.dto.MatchResponseDto;
import br.com.meli.matchsaver.service.MatchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/matches")
public class MatchController {
    @Autowired
    private MatchService matchService;

    @GetMapping
    ResponseEntity<List<MatchResponseDto>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<MatchResponseDto> getById(@PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getById(id));
    }

    @GetMapping("/trashes")
    ResponseEntity<List<MatchResponseDto>> getAllTrashes(){
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getAllTrashes());
    }

    @GetMapping("/goalless")
    ResponseEntity<List<MatchResponseDto>> getAllGoalless(){
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getAllGoalless());
    }

    @GetMapping("/club")
    ResponseEntity<List<MatchResponseDto>> getAllByClubName(@RequestParam String name, @RequestParam boolean isClubHome){
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getAllByClubName(name, isClubHome));
    }

    @GetMapping("/stadium")
    ResponseEntity<List<MatchResponseDto>> getAllByStadium(@RequestParam String name){
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getAllByStadium(name));
    }

    @PostMapping
    ResponseEntity<MatchResponseDto> save(@Valid @RequestBody MatchDto matchDto){
        MatchResponseDto newMatch = matchService.save(matchDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMatch);
    }

    @PutMapping("/{id}")
    ResponseEntity<MatchResponseDto> update(@PathVariable UUID id, @RequestBody MatchDto matchDto){
        MatchResponseDto updatedMatch = matchService.update(id, matchDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMatch);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String>delete(@PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.OK).body(matchService.delete(id));
    }
}
