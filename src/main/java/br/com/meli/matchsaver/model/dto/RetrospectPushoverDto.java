package br.com.meli.matchsaver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RetrospectPushoverDto {

    private ClubDto clubPushover;
    private int totalMatches = 0;
    private int totalWins = 0;
    private int totalLoses = 0;

}
