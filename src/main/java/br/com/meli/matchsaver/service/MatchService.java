package br.com.meli.matchsaver.service;

import br.com.meli.matchsaver.enums.Result;
import br.com.meli.matchsaver.exceptions.EntityNotFoundException;
import br.com.meli.matchsaver.exceptions.InvalidMatchTimeException;
import br.com.meli.matchsaver.model.ClubModel;
import br.com.meli.matchsaver.model.MatchModel;
import br.com.meli.matchsaver.model.StadiumModel;
import br.com.meli.matchsaver.model.dto.MatchDto;
import br.com.meli.matchsaver.repository.ClubRepository;
import br.com.meli.matchsaver.repository.MatchRepository;
import br.com.meli.matchsaver.repository.StadiumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
                    matchModel.getStadiumModel().getName(),matchModel.getDateTime().toString(), matchModel.getHomeGoals(), matchModel.getVisitingGoals());
            matchDtos.add(matchDto);
        }
        return matchDtos;
    }

    public MatchDto getById(Long id){
        MatchModel matchFound = matchRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Match not found"));

        return new MatchDto(matchFound.getHomeClub().getName(), matchFound.getVisitingClub().getName(),
                matchFound.getStadiumModel().getName(), matchFound.getDateTime().toString(), matchFound.getHomeGoals(), matchFound.getVisitingGoals());
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
                () -> new EntityNotFoundException("Club " + name));
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
                () -> new EntityNotFoundException("Stadium " + name));
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
                () -> new EntityNotFoundException("Club " + matchDto.homeClub()));

        ClubModel visitingClubModel = clubRepository.findByName(matchDto.visitingClub()).orElseThrow(
                () -> new EntityNotFoundException("Club " + matchDto.visitingClub()));

        StadiumModel stadium = stadiumRepository.findByName(matchDto.stadium()).orElseThrow(
                () -> new EntityNotFoundException("Stadium " + matchDto.stadium()));

        MatchModel matchModel = new MatchModel();
        matchModel.setHomeClub(homeClubModel);
        matchModel.setVisitingClub(visitingClubModel);
        matchModel.setHomeGoals(matchDto.homeGoals());
        matchModel.setVisitingGoals(matchDto.visitingGoals());
        matchModel.setStadiumModel(stadium);
        matchModel.setDateTime(convertDateTime(matchDto.dateTime()));

        matchModel.setResult(returnResult(matchModel));

        matchRepository.save(matchModel);

        return matchModel;
    }

    public MatchDto update(Long id, MatchDto matchDto){
        MatchModel matchFound = matchRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Match"));

        if (matchDto.homeClub() != null){
            ClubModel homeClub = clubRepository.findByName(matchDto.homeClub()).orElseThrow(
                    () -> new EntityNotFoundException("Club " + matchDto.homeClub()));
            matchFound.setHomeClub(homeClub);
        }
        if (matchDto.visitingClub() != null){
            ClubModel visitingClub = clubRepository.findByName(matchDto.visitingClub()).orElseThrow(
                    () -> new EntityNotFoundException("Club  " + matchDto.visitingClub()));
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
                    () -> new EntityNotFoundException("Club  " + matchDto.visitingClub()));
            matchFound.setStadiumModel(stadiumModel);
        }
        if (matchDto.dateTime() != null){
            matchFound.setDateTime(convertDateTime(matchDto.dateTime()));
        }

        matchFound.setResult(returnResult(matchFound));

        matchRepository.save(matchFound);
        return new MatchDto(matchFound.getHomeClub().getName(), matchFound.getVisitingClub().getName(),
                matchFound.getStadiumModel().getName(),matchFound.getDateTime().toString(), matchFound.getHomeGoals(), matchFound.getVisitingGoals());
    }

    public String delete(Long id){
        MatchModel matchFound = matchRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Match"));
        matchRepository.delete(matchFound);
        return "Match deleted successfully.";
    }
    public Result returnResult (MatchModel matchModel){
        if (matchModel.getHomeGoals() > matchModel.getVisitingGoals()){
            return Result.HOME_CLUB_WIN;
        }
        if (matchModel.getHomeGoals() < matchModel.getVisitingGoals()){
            return Result.VISITING_CLUB_WIN;
        }
        return Result.DRAW;
    }

    public LocalDateTime convertDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try {
            return LocalDateTime.parse(dateTime, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidMatchTimeException("Invalid date and time format. Use the format dd/MM/yyyy HH:mm");
        }
    }

    public void validateMatchDateTime(String dateTime){
        LocalDateTime matchDateTime = convertDateTime(dateTime);
        if (matchDateTime.getHour() < 8 || matchDateTime.getHour() > 22){
            throw new InvalidMatchTimeException("Invalid hour. Use hour after 8h and before 22h");
        }
        if (matchDateTime.isAfter(LocalDateTime.now())){
            throw new InvalidMatchTimeException("Invalid date time. Match date and time are in the future");
        }
    }

    public void validateMatchStadium(String name, LocalDateTime dateTime){
        List<MatchModel> matchModels = matchRepository.findByStadiumModelNameContainingAndDateTime(name, dateTime);
        if (!matchModels.isEmpty()){
            throw new InvalidMatchTimeException("Invalid date time. There is already a match at this date and time.");
        }
    }
}
