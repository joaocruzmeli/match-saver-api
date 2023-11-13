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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
        stadiumDto02 = new StadiumDto("morumbi", 10000L);
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

    @Test
    @DisplayName("Save new stadium Test")
    public void saveNewStadium() throws Exception {
        when(stadiumService.save(Mockito.any(stadiumDto01.getClass()))).thenReturn(stadiumDto01);

        String requestJson = objectMapper.writeValueAsString(stadiumDto01);
        String responseJson = objectMapper.writeValueAsString(stadiumDto01);

        mockMvc.perform(post("/stadiums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseJson))
                .andDo(print());

        verify(stadiumService, times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Save duplicated stadium Test")
    public void saveDuplicatedStadiumTest() throws Exception {

        when(stadiumService.save(Mockito.any(stadiumDto01.getClass()))).thenThrow(new RuntimeException(new SQLIntegrityConstraintViolationException("Duplicate entry")));

        String requestJson = objectMapper.writeValueAsString(stadiumDto01);

        mockMvc.perform(post("/stadiums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict())
                .andDo(print());

        verify(stadiumService, times(1)).save(Mockito.any(stadiumDto01.getClass()));
    }

    @Test
    @DisplayName("Save stadium with blank name Test")
    public void saveStadiumWithBlankNameTest() throws Exception {
        StadiumDto invalidStadiumDto = new StadiumDto("", 10000L);

        doThrow(new RuntimeException(new Exception())).when(stadiumService).save(invalidStadiumDto);

        String requestJson = objectMapper.writeValueAsString(invalidStadiumDto);

        mockMvc.perform(post("/stadiums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(stadiumService, times(1)).save(invalidStadiumDto);
    }

    @Test
    @DisplayName("Update stadium Test")
    public void updateStadiumTest() throws Exception {
        UUID idClub = UUID.randomUUID();

        StadiumDto updatedStadium = new StadiumDto("nogueirao", 10000L);

        when(stadiumService.update(Mockito.any(idClub.getClass()), Mockito.any(updatedStadium.getClass()))).thenReturn(updatedStadium);

        String requestJson = objectMapper.writeValueAsString(updatedStadium);
        String responseJson = objectMapper.writeValueAsString(updatedStadium);

        mockMvc.perform(put("/stadiums/{id}", idClub)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andDo(print());

        verify(stadiumService, times(1)).update(Mockito.any(idClub.getClass()), Mockito.any(updatedStadium.getClass()));
    }

    @Test
    @DisplayName("Delete stadium Test")
    public void deleteStadiumTest() throws Exception{
        UUID idClub = UUID.randomUUID();

        when(stadiumService.delete(idClub)).thenReturn("Stadium deleted successfully.");

        mockMvc.perform(delete("/stadiums/{id}", UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(stadiumService, times(1)).delete(Mockito.any());
    }

    @Test
    @DisplayName("Delete Not Found Stadium")
    public void deleteNotFoundStadium() throws Exception{
        UUID idStadium = UUID.randomUUID();

        when(stadiumService.delete(idStadium)).thenThrow(new EntityNotFoundException("Club not found"));

        mockMvc.perform(delete("/stadiums/{id}", idStadium))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
