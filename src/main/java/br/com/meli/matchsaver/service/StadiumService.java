package br.com.meli.matchsaver.service;

import br.com.meli.matchsaver.exceptions.EntityNotFoundException;
import br.com.meli.matchsaver.model.StadiumModel;
import br.com.meli.matchsaver.model.dto.StadiumDto;
import br.com.meli.matchsaver.repository.StadiumRepository;
import br.com.meli.matchsaver.utils.mapper.StadiumMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StadiumService {
    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private StadiumMapper stadiumMapper;

    public List<StadiumDto> getAll(){
        List<StadiumModel> stadiumModels = stadiumRepository.findAll();
        List<StadiumDto> stadiumDtos = new ArrayList<>();
        for (StadiumModel stadiumModel : stadiumModels) {
            stadiumDtos.add(stadiumMapper.toStadiumDto(stadiumModel));
        }
        return stadiumDtos;
    }

    public StadiumDto getById(UUID id){
        StadiumModel stadiumModelFound = stadiumRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Stadium"));
        return stadiumMapper.toStadiumDto(stadiumModelFound);
    }

    public StadiumModel save(StadiumDto stadiumDto){
        StadiumModel stadiumModel = stadiumMapper.toStadiumModel(stadiumDto);
        stadiumRepository.save(stadiumModel);
        return stadiumModel;
    }

    public StadiumDto update(UUID id, StadiumDto stadiumDto){
        StadiumModel stadiumModelFound = stadiumRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Stadium"));
        if (stadiumDto.name() != null){
            stadiumModelFound.setName(stadiumDto.name());
        }
        if (stadiumDto.capacity() != null){
            stadiumModelFound.setCapacity(stadiumDto.capacity());
        }
        stadiumRepository.save(stadiumModelFound);
        return stadiumMapper.toStadiumDto(stadiumModelFound);
    }

    public String delete(UUID id){
        StadiumModel stadiumModelFound = stadiumRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Stadium"));
        stadiumRepository.delete(stadiumModelFound);
        return "Stadium deleted successfully";
    }
}
