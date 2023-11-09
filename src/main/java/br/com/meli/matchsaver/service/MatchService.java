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
import br.com.meli.matchsaver.utils.mapper.MatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private MatchMapper matchMapper;

    public List<MatchDto> getAll(){
        List<MatchModel> matchModels = matchRepository.findAll();
        List<MatchDto> matchDtos = new ArrayList<>();
        for (MatchModel matchModel : matchModels) {
            matchDtos.add(matchMapper.toMatchDto(matchModel));
        }
        return matchDtos;
    }

    public MatchDto getById(UUID id){
        MatchModel matchModelFound = matchRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Match not found"));

        return matchMapper.toMatchDto(matchModelFound);
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
        ClubModel clubModelFound = clubRepository.findByName(name).orElseThrow(
                () -> new EntityNotFoundException("Club " + name));
        List<MatchModel> matchModels = matchRepository.findAll();
        List<MatchModel> matchClubs = new ArrayList<>();
        for (MatchModel match: matchModels) {
            boolean isHomeClub = match.getHomeClub().getName().equalsIgnoreCase(clubModelFound.getName());
            boolean isVisitingClub = match.getVisitingClub().getName().equalsIgnoreCase(clubModelFound.getName());

            if ((isHomeClub && isClubHome) || (!isClubHome && isVisitingClub)) {
                matchClubs.add(match);
            }
        }
        return matchClubs;
    }

    public List<MatchModel> getAllByStadium(String name){
        StadiumModel stadiumModelFound = stadiumRepository.findByName(name).orElseThrow(
                () -> new EntityNotFoundException("Stadium " + name));
        List<MatchModel> matchModels = matchRepository.findAll();
        List<MatchModel> matchStadiums = new ArrayList<>();
        for (MatchModel match: matchModels) {;
            if (match.getStadium().getName().equalsIgnoreCase(stadiumModelFound.getName())){
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


        validateMatchDateTime(matchDto.dateTime());
        validateMatchStadium(matchDto.stadium(), convertDateTime(matchDto.dateTime()));
        validateMatchSameClub(homeClubModel);
        validateMatchSameClub(visitingClubModel);


        //TODO -> CONVERTER UTILIZANDO O MAPPER
        MatchModel matchModel = new MatchModel();
        matchModel.setHomeClub(homeClubModel);
        matchModel.setVisitingClub(visitingClubModel);
        matchModel.setHomeGoals(matchDto.homeGoals());
        matchModel.setVisitingGoals(matchDto.visitingGoals());
        matchModel.setStadium(stadium);
        matchModel.setDateTime(convertDateTime(matchDto.dateTime()));

        matchModel.setResult(returnResult(matchModel));

        matchRepository.save(matchModel);

        return matchModel;
    }

    public MatchDto update(UUID id, MatchDto matchDto){
        validateMatchGoalsPositive(matchDto);

        MatchModel matchModelFound = matchRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Match"));

        if (matchDto.homeClub() != null){
            ClubModel homeClub = clubRepository.findByName(matchDto.homeClub()).orElseThrow(
                    () -> new EntityNotFoundException("Club " + matchDto.homeClub()));
            matchModelFound.setHomeClub(homeClub);
        }
        if (matchDto.visitingClub() != null){
            ClubModel visitingClub = clubRepository.findByName(matchDto.visitingClub()).orElseThrow(
                    () -> new EntityNotFoundException("Club  " + matchDto.visitingClub()));
            matchModelFound.setVisitingClub(visitingClub);
        }
        if (matchDto.homeGoals() != null){
            matchModelFound.setHomeGoals(matchDto.homeGoals());
        }
        if (matchDto.visitingGoals() != null){
            matchModelFound.setVisitingGoals(matchDto.visitingGoals());
        }
        if (matchDto.stadium() != null){
            StadiumModel stadiumModel = stadiumRepository.findByName(matchDto.stadium()).orElseThrow(
                    () -> new EntityNotFoundException("Club  " + matchDto.visitingClub()));
            matchModelFound.setStadium(stadiumModel);
        }
        if (matchDto.dateTime() != null){
            matchModelFound.setDateTime(convertDateTime(matchDto.dateTime()));
        }

        matchModelFound.setResult(returnResult(matchModelFound));

        matchRepository.save(matchModelFound);
        return matchMapper.toMatchDto(matchModelFound);
    }

    public String delete(UUID id){
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
            throw new InvalidMatchTimeException("Invalid date and time. Match date and time are in the future");
        }
    }

    public void validateMatchStadium(String name, LocalDateTime dateTime){
        List<MatchModel> matchModels = matchRepository.findAllByStadiumNameContainingIgnoreCaseAndDateTime(name, dateTime);
        if (!matchModels.isEmpty()){
            throw new InvalidMatchTimeException("Invalid date and time. There is already a match in this stadium at this date and time.");
        }
    }

    public void validateMatchSameClub(ClubModel clubModel){
        List<MatchModel> matchModels = matchRepository.findAllByHomeClubNameContainingIgnoreCaseOrVisitingClubNameContainingIgnoreCase(clubModel.getName(), clubModel.getName());
        if (!matchModels.isEmpty()){
            MatchModel firstMatch = matchModels.get(0);
            for (int i = 1; i < matchModels.size(); i++) {
                long durationDiff = Duration.between(firstMatch.getDateTime(), matchModels.get(i).getDateTime()).toDays();
                if (durationDiff <=2 ){
                    throw new InvalidMatchTimeException("Invalid date and time. The club '" + clubModel.getName() + "' played less than 2 days ago.");
                }
                firstMatch = matchModels.get(i);
            }
        }
    }

    public void validateMatchGoalsPositive(MatchDto matchDto){
        if (matchDto.homeGoals() < 0 || matchDto.visitingGoals() < 0 )  throw new IllegalArgumentException("goals must be greater than or equal to zero");
    }
}
