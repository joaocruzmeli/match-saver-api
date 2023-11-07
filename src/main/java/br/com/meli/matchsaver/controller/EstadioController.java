package br.com.meli.matchsaver.controller;

import br.com.meli.matchsaver.model.Estadio;
import br.com.meli.matchsaver.model.dto.ClubeDto;
import br.com.meli.matchsaver.model.dto.EstadioDto;
import br.com.meli.matchsaver.service.EstadioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estadios")
public class EstadioController {

    @Autowired
    private EstadioService estadioService;

    @GetMapping
    ResponseEntity<List<EstadioDto>> getAll(){
        return ResponseEntity.ok(estadioService.getAll());
    }

    @GetMapping(path = "{id}")
    ResponseEntity<EstadioDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(estadioService.getById(id));
    }

    @PostMapping
    ResponseEntity<EstadioDto> save(@RequestBody EstadioDto estadioDto){
        EstadioDto novoEstadio = estadioService.save(estadioDto);
        return new ResponseEntity<>(novoEstadio, HttpStatus.CREATED);
    }

    @PutMapping(path = "{id}")
    ResponseEntity<EstadioDto> update(@PathVariable Long id, @RequestBody EstadioDto estadioDto){
        EstadioDto estadioAtualizado = estadioService.update(id, estadioDto);
        return ResponseEntity.ok(estadioAtualizado);
    }

    @DeleteMapping(path = "{id}")
    ResponseEntity<String>delete(@PathVariable Long id){
        return ResponseEntity.ok(estadioService.delete(id));
    }
}
