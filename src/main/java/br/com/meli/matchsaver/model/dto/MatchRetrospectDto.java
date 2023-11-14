package br.com.meli.matchsaver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchRetrospectDto {

    private ClubDto clubDto;
    private int totalWins = 0;
    private int totalLoses = 0;
    private int totalDraws = 0;
    private int goalsScored = 0;
    private int goalsConceded = 0;

}
