package br.com.meli.matchsaver.controller;

import br.com.meli.matchsaver.model.StadiumModel;
import br.com.meli.matchsaver.model.dto.StadiumDto;
import br.com.meli.matchsaver.service.StadiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stadiums")
public class StadiumController {

    @Autowired
    private StadiumService stadiumService;

    @GetMapping
    ResponseEntity<List<StadiumDto>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(stadiumService.getAll());
    }

    @GetMapping(path = "{id}")
    ResponseEntity<StadiumDto> getById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(stadiumService.getById(id));
    }

    @PostMapping
    ResponseEntity<StadiumModel> save(@RequestBody StadiumDto stadiumDto){
        StadiumModel newStadium = stadiumService.save(stadiumDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newStadium);
    }

    @PutMapping(path = "{id}")
    ResponseEntity<StadiumDto> update(@PathVariable Long id, @RequestBody StadiumDto stadiumDto){
        StadiumDto updatedStadium = stadiumService.update(id, stadiumDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedStadium);
    }

    @DeleteMapping(path = "{id}")
    ResponseEntity<String>delete(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(stadiumService.delete(id));
    }
}
