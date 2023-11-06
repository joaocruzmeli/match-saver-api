package br.com.meli.matchsaver.controller;

import br.com.meli.matchsaver.model.dto.ClubeDto;
import br.com.meli.matchsaver.service.ClubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clubes")
public class ClubeController {

    @Autowired
    private ClubeService clubeService;

    @GetMapping
    ResponseEntity<List<ClubeDto>> getAll(){
        return ResponseEntity.ok(clubeService.getAll());
    }

    @GetMapping(path = "{id}")
    ResponseEntity<ClubeDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(clubeService.getById(id));
    }

    @PostMapping
    ResponseEntity<ClubeDto> save(@RequestBody ClubeDto clubeDto){
        ClubeDto novoClube = clubeService.save(clubeDto);
        return new ResponseEntity<>(novoClube, HttpStatus.CREATED);
    }

    @PutMapping(path = "{id}")
    ResponseEntity<ClubeDto> update(@PathVariable Long id, @RequestBody ClubeDto clubeDto){
        ClubeDto clubeAtualizado = clubeService.update(id, clubeDto);
        return ResponseEntity.ok(clubeAtualizado);
    }

    @DeleteMapping(path = "{id}")
    ResponseEntity<String>delete(@PathVariable Long id){
        return ResponseEntity.ok(clubeService.delete(id));
    }

}
