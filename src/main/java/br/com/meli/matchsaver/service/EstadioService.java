package br.com.meli.matchsaver.service;

import br.com.meli.matchsaver.model.Estadio;
import br.com.meli.matchsaver.model.dto.EstadioDto;
import br.com.meli.matchsaver.repository.EstadioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstadioService {
    @Autowired
    private EstadioRepository estadioRepository;

    public List<EstadioDto> getAll(){
        List<Estadio> estadios = estadioRepository.findAll();
        List<EstadioDto> estadiosDtos = new ArrayList<>();
        for (Estadio estadio : estadios) {
            estadiosDtos.add(new EstadioDto(estadio.getNome(), estadio.getCapacidade()));
        }
        return estadiosDtos;
    }

    public EstadioDto getById(Long id){
        Estadio estadioEncontrado = estadioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Estádio de id " + id + " não encontrado"));
        return new EstadioDto(estadioEncontrado.getNome(), estadioEncontrado.getCapacidade());
    }

    public EstadioDto save(EstadioDto estadioDto){
        Estadio estadio = new Estadio();
        estadio.setNome(estadioDto.nome());
        estadio.setCapacidade(estadio.getCapacidade());
        estadioRepository.save(estadio);
        return estadioDto;
    }

    public EstadioDto update(Long id, EstadioDto estadioDto){
        Estadio estadioEncontrado = estadioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Estádio de id " + id + " não encontrado"));
        if (estadioDto.nome() != null){
            estadioEncontrado.setNome(estadioDto.nome());
        }
        if (estadioDto.capacidade() != null){
            estadioEncontrado.setCapacidade(estadioDto.capacidade());
        }
        estadioRepository.save(estadioEncontrado);
        return new EstadioDto(estadioEncontrado.getNome(), estadioEncontrado.getCapacidade());
    }

    public String delete(Long id){
        Estadio estadioEncontrado = estadioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Estádio de id " + id + " não encontrado"));
        estadioRepository.delete(estadioEncontrado);
        return "Estádio deletado com sucesso";
    }
}
