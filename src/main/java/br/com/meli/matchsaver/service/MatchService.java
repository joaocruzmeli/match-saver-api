package br.com.meli.matchsaver.service;

import br.com.meli.matchsaver.enums.Result;
import br.com.meli.matchsaver.exceptions.EntityNotFoundException;
import br.com.meli.matchsaver.exceptions.InvalidMatchTimeException;
import br.com.meli.matchsaver.model.ClubModel;
import br.com.meli.matchsaver.model.MatchModel;
import br.com.meli.matchsaver.model.StadiumModel;
import br.com.meli.matchsaver.model.dto.MatchDto;
import br.com.meli.matchsaver.model.dto.MatchResponseDto;
import br.com.meli.matchsaver.repository.ClubRepository;
import br.com.meli.matchsaver.repository.MatchRepository;
import br.com.meli.matchsaver.repository.StadiumRepository;
import br.com.meli.matchsaver.utils.mapper.MatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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



    public List<MatchResponseDto> getAll(){
        List<MatchModel> matchModels = matchRepository.findAll();
        List<MatchResponseDto> matchDtos = new ArrayList<>();
        for (MatchModel matchModel : matchModels) {
            matchDtos.add(MatchMapper.INSTANCE.toMatchResponseDto(matchModel));
        }
        return matchDtos;
    }

    public MatchResponseDto getById(UUID id){
        MatchModel matchModelFound = matchRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Match not found"));

        return MatchMapper.INSTANCE.toMatchResponseDto(matchModelFound);
    }

    public List<MatchResponseDto> getAllTrashes(){
        List<MatchModel> matchModels = matchRepository.findAll();
        List<MatchResponseDto> matchTrashes = new ArrayList<>();
        for (MatchModel match: matchModels) {
            int diffGoals = Math.abs(match.getHomeGoals() - match.getVisitingGoals());
            if (diffGoals >= 3){
                matchTrashes.add(MatchMapper.INSTANCE.toMatchResponseDto(match));
            }
        }
        return matchTrashes;
    }

    public List<MatchResponseDto> getAllGoalless(){
        List<MatchModel> matchModels = matchRepository.findAll();
        List<MatchResponseDto> matchGoalless = new ArrayList<>();
        for (MatchModel match: matchModels) {
            int diffGoals = Math.abs(match.getHomeGoals() - match.getVisitingGoals());
            if (diffGoals == 0){
                matchGoalless.add(MatchMapper.INSTANCE.toMatchResponseDto(match));
            }
        }
        return matchGoalless;
    }

    public List<MatchResponseDto> getAllByClubName(String name, boolean isClubHome){
        ClubModel clubModelFound = clubRepository.findByName(name).orElseThrow(
                () -> new EntityNotFoundException("Club " + name));
        List<MatchModel> matchModels = matchRepository.findAll();
        List<MatchResponseDto> matchClubs = new ArrayList<>();
        for (MatchModel match: matchModels) {
            boolean isHomeClub = match.getHomeClub().getName().equalsIgnoreCase(clubModelFound.getName());
            boolean isVisitingClub = match.getVisitingClub().getName().equalsIgnoreCase(clubModelFound.getName());

            if ((isHomeClub && isClubHome) || (!isClubHome && isVisitingClub)) {
                matchClubs.add(MatchMapper.INSTANCE.toMatchResponseDto(match));
            }
        }
        return matchClubs;
    }

    public List<MatchResponseDto> getAllByStadium(String name){
        StadiumModel stadiumModelFound = stadiumRepository.findByName(name).orElseThrow(
                () -> new EntityNotFoundException("Stadium " + name));
        List<MatchModel> matchModels = matchRepository.findAll();
        List<MatchResponseDto> matchStadiums = new ArrayList<>();
        for (MatchModel match: matchModels) {
            if (match.getStadium().getName().equalsIgnoreCase(stadiumModelFound.getName())){
                matchStadiums.add(MatchMapper.INSTANCE.toMatchResponseDto(match));
            }
        }
        return matchStadiums;
    }


    public MatchResponseDto save(MatchDto matchDto){

        ClubModel homeClubModel = clubRepository.findByName(matchDto.getHomeClub()).orElseThrow(
                () -> new EntityNotFoundException("Club " + matchDto.getHomeClub()));

        ClubModel visitingClubModel = clubRepository.findByName(matchDto.getVisitingClub()).orElseThrow(
                () -> new EntityNotFoundException("Club " + matchDto.getVisitingClub()));

        StadiumModel stadium = stadiumRepository.findByName(matchDto.getStadium()).orElseThrow(
                () -> new EntityNotFoundException("Stadium " + matchDto.getStadium()));


        validateMatchDateTime(matchDto.getDateTime());
        validateMatchStadium(matchDto.getStadium(), convertDateTime(matchDto.getDateTime()));
        validateMatchSameClubInDays(homeClubModel, convertDateTime(matchDto.getDateTime()));
        validateMatchSameClubInDays(visitingClubModel, convertDateTime(matchDto.getDateTime()));
        validateMatchClubs(matchDto.getHomeClub(), matchDto.getVisitingClub());

        //TODO Estudar como melhorar esse mapper
        MatchModel matchModel = MatchMapper.INSTANCE.toMatchModel(matchDto);
        matchModel.setDateTime(convertDateTime(matchDto.getDateTime()));
        matchModel.setVisitingClub(visitingClubModel);
        matchModel.setHomeClub(homeClubModel);
        matchModel.setStadium(stadium);
        matchModel.setResult(returnResult(matchModel));

        matchRepository.save(matchModel);

        return MatchMapper.INSTANCE.toMatchResponseDto(matchModel);
    }

    // TODO REFATORAR ESSE MÉTODO
    public MatchResponseDto update(UUID id, MatchDto matchDto){
        validateMatchGoalsPositive(matchDto);

        MatchModel matchModelFound = matchRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Match"));

        if (matchDto.getHomeClub() != null){
            ClubModel homeClub = clubRepository.findByName(matchDto.getHomeClub()).orElseThrow(
                    () -> new EntityNotFoundException("Club " + matchDto.getHomeClub()));
            validateMatchClubs(homeClub.getName(), matchModelFound.getVisitingClub().getName());
            matchModelFound.setHomeClub(homeClub);
        }
        if (matchDto.getVisitingClub() != null){
            ClubModel visitingClub = clubRepository.findByName(matchDto.getVisitingClub()).orElseThrow(
                    () -> new EntityNotFoundException("Club  " + matchDto.getVisitingClub()));
            validateMatchClubs(matchModelFound.getHomeClub().getName(), visitingClub.getName());
            matchModelFound.setVisitingClub(visitingClub);
        }
        if (matchDto.getHomeGoals() != null){
            matchModelFound.setHomeGoals(matchDto.getHomeGoals());
        }
        if (matchDto.getVisitingGoals() != null){
            matchModelFound.setVisitingGoals(matchDto.getVisitingGoals());
        }
        if (matchDto.getStadium() != null){
            StadiumModel stadiumModel = stadiumRepository.findByName(matchDto.getStadium()).orElseThrow(
                    () -> new EntityNotFoundException("Club  " + matchDto.getVisitingClub()));
            if (matchDto.getDateTime() != null){
                validateMatchStadium(matchDto.getStadium(), convertDateTime(matchDto.getDateTime()));
            } else {
                validateMatchStadium(matchDto.getStadium(), matchModelFound.getDateTime());
            }
            matchModelFound.setStadium(stadiumModel);
        }
        if (matchDto.getDateTime() != null){
            if (matchDto.getStadium() != null){
                validateMatchStadium(matchDto.getStadium(), convertDateTime(matchDto.getDateTime()));
            } else {
                validateMatchStadium(matchModelFound.getStadium().getName(), convertDateTime(matchDto.getDateTime()));
            }
            matchModelFound.setDateTime(convertDateTime(matchDto.getDateTime()));
        }

        matchModelFound.setResult(returnResult(matchModelFound));

        matchRepository.save(matchModelFound);
        return MatchMapper.INSTANCE.toMatchResponseDto(matchModelFound);
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

    public void validateMatchSameClubInDays(ClubModel clubModel,LocalDateTime dateTime){
        List<MatchModel> matchModels = matchRepository.findAllByHomeClubNameContainingIgnoreCaseOrVisitingClubNameContainingIgnoreCase(clubModel.getName(), clubModel.getName());
        if (!matchModels.isEmpty()){
            for (MatchModel match : matchModels) {
                long durationDiff = Math.abs(Duration.between(match.getDateTime(), dateTime).toDays());
                if (durationDiff <= 2) {
                    throw new InvalidMatchTimeException("Invalid date and time. The club '" + clubModel.getName() + "' played less than 2 days ago.");
                }
            }
        }
    }

    public void validateMatchGoalsPositive(MatchDto matchDto){
        if (matchDto.getHomeGoals() != null && matchDto.getHomeGoals() < 0) {
            throw new IllegalArgumentException("Invalid goals. goals must be greater than or equal to zero");
        }
        if (matchDto.getVisitingGoals() != null && matchDto.getVisitingGoals() < 0) {
            throw new IllegalArgumentException("Invalid goals. goals must be greater than or equal to zero");
        }
    }

    public void validateMatchClubs(String homeClub, String visitingClub){
        if (homeClub.equalsIgnoreCase(visitingClub)) throw new IllegalArgumentException("Invalid clubs. the home club cannot be the same as the visiting club");
    }
}
