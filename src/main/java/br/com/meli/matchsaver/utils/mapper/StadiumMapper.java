package br.com.meli.matchsaver.utils.mapper;

import br.com.meli.matchsaver.model.StadiumModel;
import br.com.meli.matchsaver.model.dto.StadiumDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StadiumMapper {

    StadiumMapper INSTANCE = Mappers.getMapper(StadiumMapper.class);

    StadiumDto toStadiumDTO(StadiumModel stadiumModel);
    StadiumModel toStadiumModel(StadiumDto stadiumDto);

    StadiumModel map(StadiumDto stadiumDto);
    StadiumModel map(String stadium);
}
