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

@Service
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubMapper clubMapper;

    public List<ClubDto> getAll(){
        List<ClubModel> clubModels = clubRepository.findAll();
        List<ClubDto> clubDtos = new ArrayList<>();
        for (ClubModel clubModel : clubModels) {
            clubDtos.add(clubMapper.toClubDto(clubModel));
        }
        return clubDtos;
    }

    public ClubDto getById(Long id){
        ClubModel clubModelFound = clubRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Club"));
        return clubMapper.toClubDto(clubModelFound);
    }

    public ClubModel save(ClubDto clubDto){
        ClubModel clubModel = clubMapper.toClubModel(clubDto);
        clubRepository.save(clubModel);
        return clubModel;
    }

    public ClubDto update(Long id, ClubDto clubDto){
        ClubModel clubModelFound = clubRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Club"));
        if (clubDto.name() != null){
            clubModelFound.setName(clubDto.name());
        }
        clubRepository.save(clubModelFound);
        return clubMapper.toClubDto(clubModelFound);
    }

    public String delete(Long id){
        ClubModel clubModelFound = clubRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Club"));
        clubRepository.delete(clubModelFound);
        return "Club deleted successfully.";
    }

}
