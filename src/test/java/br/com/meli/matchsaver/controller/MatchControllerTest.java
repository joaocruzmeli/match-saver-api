package br.com.meli.matchsaver.controller;

import br.com.meli.matchsaver.enums.Result;
import br.com.meli.matchsaver.exceptions.EntityNotFoundException;
import br.com.meli.matchsaver.exceptions.ErrorMessage;
import br.com.meli.matchsaver.model.dto.ClubDto;
import br.com.meli.matchsaver.model.dto.MatchDto;
import br.com.meli.matchsaver.model.dto.MatchResponseDto;
import br.com.meli.matchsaver.model.dto.StadiumDto;
import br.com.meli.matchsaver.service.MatchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchController.class)
public class MatchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MatchService matchService;

    private MatchDto matchDto;
    private MatchResponseDto matchResponseDto;

    private ClubDto homeClubDto;
    private ClubDto visitingClubDto;
    private StadiumDto stadiumDto;


    @BeforeEach
    public void config(){
        homeClubDto = new ClubDto("flamengo");
        visitingClubDto = new ClubDto("vasco");
        stadiumDto = new StadiumDto("nogueirao", 10000L);
        matchDto = new MatchDto(homeClubDto.getName(), visitingClubDto.getName(), stadiumDto.getName(), "20/10/2023 15:40", 5, 2);
        matchResponseDto = new MatchResponseDto(UUID.randomUUID(), homeClubDto, visitingClubDto, stadiumDto,
                "20/10/2023 15:40", 5, 2, Result.HOME_CLUB_WIN );
    }

    @Test
    @DisplayName("Get All Matches Test")
    public void getAllMatchesTest() throws Exception {
        List<MatchResponseDto> expectedMatchesList = Arrays.asList(matchResponseDto);

        when(matchService.getAll()).thenReturn(expectedMatchesList);

        String responseJson = objectMapper.writeValueAsString(expectedMatchesList);

        mockMvc.perform(get("/matches")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andDo(print());

        verify(matchService, times(1)).getAll();
    }

    @Test
    @DisplayName("Get Match By Id Test")
    public void getMatchByIdTest() throws Exception {
        UUID idMatchResponse = matchResponseDto.getId();

        when(matchService.getById(idMatchResponse)).thenReturn(matchResponseDto);

        String responseJson = objectMapper.writeValueAsString(matchResponseDto);

        mockMvc.perform(get("/matches/{id}", idMatchResponse)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andDo(print());

        verify(matchService, times(1)).getById(idMatchResponse);
    }

    @Test
    @DisplayName("Get Match By Non-Exist Id Test")
    public void getMatchByIdNonExistTest() throws Exception {
        UUID idMatchResponse = UUID.randomUUID();

        when(matchService.getById(idMatchResponse)).thenThrow(new EntityNotFoundException("Match not found"));

        ErrorMessage errorResponse = new ErrorMessage(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "Match not found");
        String responseJson = objectMapper.writeValueAsString(errorResponse);


        mockMvc.perform(get("/matches/{id}", idMatchResponse)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(responseJson))
                .andDo(print());

        verify(matchService, times(1)).getById(idMatchResponse);
    }

    @Test
    @DisplayName("Get All Trashes Matchs Test")
    public void getAllTrashesTest() throws Exception {
        MatchResponseDto matchTrashDto = new MatchResponseDto(UUID.randomUUID(), homeClubDto, visitingClubDto, stadiumDto,
                "20/10/2023 15:40", 3, 0, Result.HOME_CLUB_WIN );

        List<MatchResponseDto> expectedMatchesList = Arrays.asList(matchTrashDto);

        when(matchService.getAllTrashes()).thenReturn(expectedMatchesList);

        String responseJson = objectMapper.writeValueAsString(expectedMatchesList);

        mockMvc.perform(get("/matches/trashes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andDo(print());

        verify(matchService, times(1)).getAllTrashes();
    }

    @Test
    @DisplayName("Get All Goalless Matches Test")
    public void getAllGoallessTest() throws Exception {
        MatchResponseDto matchGoallessDto = new MatchResponseDto(UUID.randomUUID(), homeClubDto, visitingClubDto, stadiumDto,
                "20/10/2023 15:40", 0, 0, Result.DRAW );

        List<MatchResponseDto> expectedMatchesList = Arrays.asList(matchGoallessDto);

        when(matchService.getAllGoalless()).thenReturn(expectedMatchesList);

        String responseJson = objectMapper.writeValueAsString(expectedMatchesList);

        mockMvc.perform(get("/matches/goalless")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andDo(print());

        verify(matchService, times(1)).getAllGoalless();
    }

    @Test
    @DisplayName("Get All Matches By Stadium Name Test")
    public void getAllByStadiumNameTest() throws Exception {
        String stadiumName = matchResponseDto.getStadium().getName();

        List<MatchResponseDto> expectedMatchesList = Arrays.asList(matchResponseDto);

        when(matchService.getAllByStadium(stadiumName)).thenReturn(expectedMatchesList);

        String responseJson = objectMapper.writeValueAsString(expectedMatchesList);

        mockMvc.perform(get("/matches/stadium")
                        .param("name", stadiumName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseJson))
                .andDo(print());

        verify(matchService, times(1)).getAllByStadium(stadiumName);
    }



}
