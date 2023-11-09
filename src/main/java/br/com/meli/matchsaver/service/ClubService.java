package br.com.meli.matchsaver.service;

import br.com.meli.matchsaver.exceptions.EntityNotFoundException;
import br.com.meli.matchsaver.model.ClubModel;
import br.com.meli.matchsaver.model.dto.ClubDto;
import br.com.meli.matchsaver.repository.ClubRepository;
import br.com.meli.matchsaver.utils.mapper.ClubMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;

    public List<ClubDto> getAll(){
        List<ClubModel> clubModels = clubRepository.findAll();
        List<ClubDto> clubDtos = new ArrayList<>();
        for (ClubModel clubModel : clubModels) {
            clubDtos.add(ClubMapper.INSTANCE.toClubDTO(clubModel));
        }
        return clubDtos;
    }

    public ClubDto getById(UUID id){
        ClubModel clubModelFound = clubRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Club"));
        return ClubMapper.INSTANCE.toClubDTO(clubModelFound);
    }

    public ClubDto save(ClubDto clubDto){
        ClubModel clubModel = ClubMapper.INSTANCE.toClubModel(clubDto);
        clubRepository.save(clubModel);
        return ClubMapper.INSTANCE.toClubDTO(clubModel);
    }

    public ClubDto update(UUID id, ClubDto clubDto){
        ClubModel clubModelFound = clubRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Club"));
        if (clubDto.getName() != null){
            clubModelFound.setName(clubDto.getName());
        }
        clubRepository.save(clubModelFound);
        return ClubMapper.INSTANCE.toClubDTO(clubModelFound);
    }

    public String delete(UUID id){
        ClubModel clubModelFound = clubRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Club"));
        clubRepository.delete(clubModelFound);
        return "Club deleted successfully.";
    }

}
