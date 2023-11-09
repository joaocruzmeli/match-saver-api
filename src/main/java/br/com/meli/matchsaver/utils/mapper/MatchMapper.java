package br.com.meli.matchsaver.utils.mapper;

import br.com.meli.matchsaver.exceptions.InvalidMatchTimeException;
import br.com.meli.matchsaver.model.MatchModel;
import br.com.meli.matchsaver.model.dto.MatchDto;
import br.com.meli.matchsaver.model.dto.MatchResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Mapper(uses = { ClubMapper.class, StadiumMapper.class })
public interface MatchMapper {
    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    @Mapping(target = "homeClub", source = "homeClub")
    @Mapping(target = "visitingClub", source = "visitingClub")
    @Mapping(target = "dateTime", expression = "java(convertDateTime(matchDto.getDateTime()))")
    MatchModel toMatchModel(MatchDto matchDto);

    MatchResponseDto toMatchResponseDto(MatchModel matchModel);

    default LocalDateTime convertDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try {
            return LocalDateTime.parse(dateTime, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidMatchTimeException("Invalid date and time format. Use the format dd/MM/yyyy HH:mm");
        }
    }
}

