package br.com.meli.matchsaver.utils.mapper;

import br.com.meli.matchsaver.model.ClubModel;
import br.com.meli.matchsaver.model.dto.ClubDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ClubMapper {
    public ClubModel toClubModel (ClubDto clubDto){
        ClubModel clubModel = new ClubModel();
        BeanUtils.copyProperties(clubDto, clubModel);
        return clubModel;
    }

    public ClubDto toClubDto (ClubModel clubModel){
        return new ClubDto(clubModel.getName());
    }
}
