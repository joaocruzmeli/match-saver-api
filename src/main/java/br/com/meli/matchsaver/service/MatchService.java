package br.com.meli.matchsaver.service;

import br.com.meli.matchsaver.enums.Result;
import br.com.meli.matchsaver.model.ClubModel;
import br.com.meli.matchsaver.model.MatchModel;
import br.com.meli.matchsaver.model.StadiumModel;
import br.com.meli.matchsaver.model.dto.MatchDto;
import br.com.meli.matchsaver.repository.ClubRepository;
import br.com.meli.matchsaver.repository.MatchRepository;
import br.com.meli.matchsaver.repository.StadiumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private StadiumRepository stadiumRepository;

    public List<MatchDto> getAll(){
        List<MatchModel> matchModels = matchRepository.findAll();
        List<MatchDto> matchDtos = new ArrayList<>();
        for (MatchModel matchModel : matchModels) {
            MatchDto matchDto = new MatchDto(matchModel.getHomeClub().getName(), matchModel.getVisitingClub().getName(),
                    matchModel.getStadiumModel().getName(), matchModel.getHomeGoals(), matchModel.getVisitingGoals());
            matchDtos.add(matchDto);
        }
        return matchDtos;
    }

    public MatchDto getById(Long id){
        MatchModel matchFound = matchRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Match not found"));
        return new MatchDto(matchFound.getHomeClub().getName(), matchFound.getVisitingClub().getName(),
                matchFound.getStadiumModel().getName(), matchFound.getHomeGoals(), matchFound.getVisitingGoals());
    }

    public List<MatchModel> getAllTrashes(){
        List<MatchModel> matchModels = matchRepository.findAll();
        List<MatchModel> matchTrashes = new ArrayList<>();
        for (MatchModel match: matchModels) {
            int diffGoals = Math.abs(match.getHomeGoals() - match.getVisitingGoals());
            if (diffGoals >= 3){
                matchTrashes.add(match);
            }
        }
        return matchTrashes;
    }

    public List<MatchModel> getAllGoalless(){
        List<MatchModel> matchModels = matchRepository.findAll();
        List<MatchModel> matchGoalless = new ArrayList<>();
        for (MatchModel match: matchModels) {
            int diffGoals = Math.abs(match.getHomeGoals() - match.getVisitingGoals());
            if (diffGoals == 0){
                matchGoalless.add(match);
            }
        }
        return matchGoalless;
    }

    public List<MatchModel> getAllByClubName(String name, boolean isClubHome){
        ClubModel clubModel = clubRepository.findByName(name).orElseThrow(
                () -> new RuntimeException("Club " + name + " not found"));
        List<MatchModel> matchModels = matchRepository.findAll();
        List<MatchModel> matchClubs = new ArrayList<>();
        for (MatchModel match: matchModels) {
            boolean isHomeClub = match.getHomeClub().getName().equalsIgnoreCase(clubModel.getName());
            boolean isVisitingClub = match.getVisitingClub().getName().equalsIgnoreCase(clubModel.getName());

            if ((isHomeClub && isClubHome) || (!isClubHome && isVisitingClub)) {
                matchClubs.add(match);
            }
        }
        return matchClubs;
    }

    public List<MatchModel> getAllByStadium(String name){
        StadiumModel stadiumModel = stadiumRepository.findByName(name).orElseThrow(
                () -> new RuntimeException("Stadium " + name + " not found"));
        List<MatchModel> matchModels = matchRepository.findAll();
        List<MatchModel> matchStadiums = new ArrayList<>();
        for (MatchModel match: matchModels) {;
            if (match.getStadiumModel().getName().equalsIgnoreCase(stadiumModel.getName())){
                matchStadiums.add(match);
            }
        }
        return matchStadiums;
    }


    public MatchModel save(MatchDto matchDto){

        ClubModel homeClubModel = clubRepository.findByName(matchDto.homeClub()).orElseThrow(
                () -> new RuntimeException("Club " + matchDto.homeClub() + " not found"));

        ClubModel visitingClubModel = clubRepository.findByName(matchDto.visitingClub()).orElseThrow(
                () -> new RuntimeException("Club " + matchDto.visitingClub() + " not found"));

        StadiumModel stadium = stadiumRepository.findByName(matchDto.stadium()).orElseThrow(
                () -> new RuntimeException("Stadium " + matchDto.stadium() + " not found"));

        MatchModel matchModel = new MatchModel();
        matchModel.setHomeClub(homeClubModel);
        matchModel.setVisitingClub(visitingClubModel);
        matchModel.setHomeGoals(matchDto.homeGoals());
        matchModel.setVisitingGoals(matchDto.visitingGoals());
        matchModel.setStadiumModel(stadium);

        matchModel.setResult(retornaResultado(matchModel));

        matchRepository.save(matchModel);

        return matchModel;
    }

    public MatchDto update(Long id, MatchDto matchDto){
        MatchModel matchFound = matchRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Match not found"));

        if (matchDto.homeClub() != null){
            ClubModel homeClub = clubRepository.findByName(matchDto.homeClub()).orElseThrow(
                    () -> new RuntimeException("Club " + matchDto.homeClub() + " not found"));
            matchFound.setHomeClub(homeClub);
        }
        if (matchDto.visitingClub() != null){
            ClubModel visitingClub = clubRepository.findByName(matchDto.visitingClub()).orElseThrow(
                    () -> new RuntimeException("Club  " + matchDto.visitingClub() + " not found"));
            matchFound.setVisitingClub(visitingClub);
        }
        if (matchDto.homeGoals() != null){
            matchFound.setHomeGoals(matchDto.homeGoals());
        }
        if (matchDto.visitingGoals() != null){
            matchFound.setVisitingGoals(matchDto.visitingGoals());
        }
        if (matchDto.stadium() != null){
            StadiumModel stadiumModel = stadiumRepository.findByName(matchDto.stadium()).orElseThrow(
                    () -> new RuntimeException("Club  " + matchDto.visitingClub() + " not found"));
            matchFound.setStadiumModel(stadiumModel);
        }

        matchFound.setResult(retornaResultado(matchFound));

        matchRepository.save(matchFound);
        return new MatchDto(matchFound.getHomeClub().getName(), matchFound.getVisitingClub().getName(),
                matchFound.getStadiumModel().getName(), matchFound.getHomeGoals(), matchFound.getVisitingGoals());
    }

    public String delete(Long id){
        MatchModel matchFound = matchRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Match not found"));
        matchRepository.delete(matchFound);
        return "Match deleted successfully.";
    }
    public Result retornaResultado (MatchModel matchModel){
        if (matchModel.getHomeGoals() > matchModel.getVisitingGoals()){
            return Result.HOME_CLUB_WIN;
        }
        if (matchModel.getHomeGoals() < matchModel.getVisitingGoals()){
            return Result.VISITING_CLUB_WIN;
        }
        return Result.DRAW;
    }
}
