package br.com.meli.matchsaver.utils.mapper;

import br.com.meli.matchsaver.model.ClubModel;
import br.com.meli.matchsaver.model.dto.ClubDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClubMapper {

    ClubMapper INSTANCE = Mappers.getMapper(ClubMapper.class);

    ClubDto toClubDTO(ClubModel clubModel);

    @Mapping(target = "homeMatches", ignore = true)
    @Mapping(target = "visitingMatches", ignore = true)
    ClubModel toClubModel(ClubDto clubDto);

    ClubModel map(String name);
}
