package br.com.meli.matchsaver.utils.mapper;

import br.com.meli.matchsaver.model.StadiumModel;
import br.com.meli.matchsaver.model.dto.StadiumDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class StadiumMapper {
    public StadiumModel toStadiumModel(StadiumDto stadiumDto){
        StadiumModel stadiumModel = new StadiumModel();
        BeanUtils.copyProperties(stadiumDto, stadiumModel);
        return stadiumModel;
    }

    public StadiumDto toStadiumDto(StadiumModel stadiumModel){
        return new StadiumDto(stadiumModel.getName(), stadiumModel.getCapacity());
    }
}
