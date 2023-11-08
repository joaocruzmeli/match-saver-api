package br.com.meli.matchsaver.utils.mapper;

import br.com.meli.matchsaver.model.MatchModel;
import br.com.meli.matchsaver.model.dto.MatchDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {

    public MatchModel toMatchModel (MatchDto matchDto){
        MatchModel matchModel = new MatchModel();
        BeanUtils.copyProperties(matchDto, matchModel);
        return matchModel;
    }

    public MatchDto toMatchDto (MatchModel matchModel){
        MatchDto matchDto = new MatchDto(
                matchModel.getHomeClub().getName(),
                matchModel.getVisitingClub().getName(),
                matchModel.getStadiumModel().getName(),
                matchModel.getDateTime().toString(),
                matchModel.getHomeGoals(),
                matchModel.getVisitingGoals());
        return matchDto;
    }
}
