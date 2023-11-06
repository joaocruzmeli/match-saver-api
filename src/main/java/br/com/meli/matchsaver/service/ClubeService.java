package br.com.meli.matchsaver.service;

import br.com.meli.matchsaver.model.Clube;
import br.com.meli.matchsaver.model.dto.ClubeDto;
import br.com.meli.matchsaver.repository.ClubeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClubeService {

    @Autowired
    private ClubeRepository clubeRepository;

    public List<ClubeDto> getAll(){
        List<Clube> clubes = clubeRepository.findAll();
        List<ClubeDto> clubesDtos = new ArrayList<>();
        for (Clube clube : clubes) {
            clubesDtos.add(new ClubeDto(clube.getNome()));
        }
        return clubesDtos;
    }

    public ClubeDto getById(Long id){
        Clube clubeEncontrado = clubeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Clube de id " + id + " não encontrado"));
        return new ClubeDto(clubeEncontrado.getNome());
    }

    public ClubeDto save(ClubeDto clubeDto){
        Clube clube = new Clube();
        clube.setNome(clubeDto.nome());
        clubeRepository.save(clube);
        return clubeDto;
    }

    public ClubeDto update(Long id, ClubeDto clubeDto){
        Clube clubeEncontrado = clubeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Clube de id " + id + " não encontrado"));
        if (clubeDto.nome() != null){
            clubeEncontrado.setNome(clubeDto.nome());
        }
        clubeRepository.save(clubeEncontrado);
        return new ClubeDto(clubeEncontrado.getNome());
    }

    public String delete(Long id){
        Clube clubeEncontrado = clubeRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Clube de id " + id + " não encontrado"));
        clubeRepository.delete(clubeEncontrado);
        return "Clube deletado com sucesso";
    }

}
