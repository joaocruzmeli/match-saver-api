package br.com.meli.matchsaver.controller;

import br.com.meli.matchsaver.exceptions.EntityNotFoundException;
import br.com.meli.matchsaver.exceptions.ErrorMessage;
import br.com.meli.matchsaver.model.dto.ClubDto;
import br.com.meli.matchsaver.model.dto.StadiumDto;
import br.com.meli.matchsaver.service.StadiumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StadiumController.class)
public class StadiumControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StadiumService stadiumService;

    private StadiumDto stadiumDto01;
    private StadiumDto stadiumDto02;


    @BeforeEach
    public void config(){
        stadiumDto01 = new StadiumDto("nogueirao", 10000L);
        stadiumDto01 = new StadiumDto("morumbi", 10000L);
    }

    @Test
    @DisplayName("Get All Stadiums Test")
    public void getAllStadiumsTest() throws Exception {
        List<StadiumDto> expectedStadiumsList = Arrays.asList(stadiumDto01, stadiumDto02);

        when(stadiumService.getAll()).thenReturn(expectedStadiumsList);

        String responseJson = objectMapper.writeValueAsString(expectedStadiumsList);

        mockMvc.perform(get("/stadiums")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andDo(print());

        verify(stadiumService, times(1)).getAll();
    }

    @Test
    @DisplayName("Get Stadium By Id Test")
    public void getClubByIdTest() throws Exception {
        UUID idStadium = UUID.randomUUID();
        stadiumDto01.setId(idStadium);

        when(stadiumService.getById(idStadium)).thenReturn(stadiumDto01);

        String responseJson = objectMapper.writeValueAsString(stadiumDto01);

        mockMvc.perform(get("/stadiums/{id}", idStadium)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andDo(print());

        verify(stadiumService, times(1)).getById(idStadium);
    }

    @Test
    @DisplayName("Get Stadium By Non-Exist Id Test")
    public void getStadiumByIdNonExistTest() throws Exception {
        UUID idStadium = UUID.randomUUID();

        when(stadiumService.getById(idStadium)).thenThrow(new EntityNotFoundException("Stadium not found"));

        ErrorMessage errorResponse = new ErrorMessage(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "Stadium not found");
        String responseJson = objectMapper.writeValueAsString(errorResponse);


        mockMvc.perform(get("/stadiums/{id}", idStadium)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(responseJson))
                .andDo(print());

        verify(stadiumService, times(1)).getById(idStadium);
    }
}
