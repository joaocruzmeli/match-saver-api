package br.com.meli.matchsaver.service;

import br.com.meli.matchsaver.exceptions.EntityNotFoundException;
import br.com.meli.matchsaver.model.StadiumModel;
import br.com.meli.matchsaver.model.dto.StadiumDto;
import br.com.meli.matchsaver.repository.StadiumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StadiumService {
    @Autowired
    private StadiumRepository stadiumRepository;

    public List<StadiumDto> getAll(){
        List<StadiumModel> stadiumModels = stadiumRepository.findAll();
        List<StadiumDto> stadiumDtos = new ArrayList<>();
        for (StadiumModel stadiumModel : stadiumModels) {
            stadiumDtos.add(new StadiumDto(stadiumModel.getName(), stadiumModel.getCapacity()));
        }
        return stadiumDtos;
    }

    public StadiumDto getById(Long id){
        StadiumModel stadiumFound = stadiumRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Stadium"));
        return new StadiumDto(stadiumFound.getName(), stadiumFound.getCapacity());
    }

    public StadiumModel save(StadiumDto stadiumDto){
        StadiumModel stadiumModel = new StadiumModel();
        stadiumModel.setName(stadiumDto.name());
        stadiumModel.setCapacity(stadiumDto.capacity());
        stadiumRepository.save(stadiumModel);
        return stadiumModel;
    }

    public StadiumDto update(Long id, StadiumDto stadiumDto){
        StadiumModel stadiumFound = stadiumRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Stadium"));
        if (stadiumDto.name() != null){
            stadiumFound.setName(stadiumDto.name());
        }
        if (stadiumDto.capacity() != null){
            stadiumFound.setCapacity(stadiumDto.capacity());
        }
        stadiumRepository.save(stadiumFound);
        return new StadiumDto(stadiumFound.getName(), stadiumFound.getCapacity());
    }

    public String delete(Long id){
        StadiumModel stadiumFound = stadiumRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Stadium"));
        stadiumRepository.delete(stadiumFound);
        return "Stadium deleted successfully";
    }
}
