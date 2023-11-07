package br.com.meli.matchsaver.controller;

import br.com.meli.matchsaver.model.ClubModel;
import br.com.meli.matchsaver.model.dto.ClubDto;
import br.com.meli.matchsaver.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubs")
public class ClubController {

    @Autowired
    private ClubService clubService;

    @GetMapping
    ResponseEntity<List<ClubDto>> getAll(){
        return ResponseEntity.ok(clubService.getAll());
    }

    @GetMapping(path = "{id}")
    ResponseEntity<ClubDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(clubService.getById(id));
    }

    @PostMapping
    ResponseEntity<ClubModel> save(@RequestBody ClubDto clubDto){
        ClubModel newClub = clubService.save(clubDto);
        return new ResponseEntity<>(newClub, HttpStatus.CREATED);
    }

    @PutMapping(path = "{id}")
    ResponseEntity<ClubDto> update(@PathVariable Long id, @RequestBody ClubDto clubDto){
        ClubDto updatedClub = clubService.update(id, clubDto);
        return ResponseEntity.ok(updatedClub);
    }

    @DeleteMapping(path = "{id}")
    ResponseEntity<String>delete(@PathVariable Long id){
        return ResponseEntity.ok(clubService.delete(id));
    }

}
