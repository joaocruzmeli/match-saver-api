package br.com.meli.matchsaver.controller;

import br.com.meli.matchsaver.exceptions.EntityNotFoundException;
import br.com.meli.matchsaver.exceptions.ErrorMessage;
import br.com.meli.matchsaver.model.dto.ClubDto;
import br.com.meli.matchsaver.service.ClubService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClubController.class)
public class ClubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClubService clubService;

    @Autowired
    private ObjectMapper objectMapper;

    private ClubDto homeClubDto;
    private ClubDto visitingClubDto;
    private ClubDto repeatedHomeClubDto;
    private ClubDto repeatedVisitingClubDto;


    @BeforeEach
    public void config(){
        homeClubDto = new ClubDto("flamengo");
        repeatedHomeClubDto = new ClubDto("flamengo");
        visitingClubDto = new ClubDto("vasco");
        repeatedVisitingClubDto = new ClubDto("vasco");
    }

    @Test
    @DisplayName("Get All Clubs Test")
    public void getAllClubsTest() throws Exception {
        List<ClubDto> expectedClubsList = Arrays.asList(homeClubDto, visitingClubDto);

        when(clubService.getAll()).thenReturn(expectedClubsList);

        String responseJson = objectMapper.writeValueAsString(expectedClubsList);

        mockMvc.perform(get("/clubs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andDo(print());

        verify(clubService, times(1)).getAll();
    }

    @Test
    @DisplayName("Get Club By Id Test")
    public void getClubByIdTest() throws Exception {
        UUID idClub = UUID.randomUUID();
        homeClubDto.setId(idClub);

        when(clubService.getById(idClub)).thenReturn(homeClubDto);

        String responseJson = objectMapper.writeValueAsString(homeClubDto);

        mockMvc.perform(get("/clubs/{id}", idClub)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andDo(print());

        verify(clubService, times(1)).getById(idClub);
    }

    @Test
    @DisplayName("Get Club By Non-Exist Id Test")
    public void getClubByIdNonExistTest() throws Exception {
        UUID idClub = UUID.randomUUID();

        when(clubService.getById(idClub)).thenThrow(new EntityNotFoundException("Club not found"));

        ErrorMessage errorResponse = new ErrorMessage(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "Club not found");
        String responseJson = objectMapper.writeValueAsString(errorResponse);


        mockMvc.perform(get("/clubs/{id}", idClub)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(responseJson))
                .andDo(print());

        verify(clubService, times(1)).getById(idClub);
    }

    @Test
    @DisplayName("Save new club Test")
    public void saveNewClub() throws Exception {
        when(clubService.save(Mockito.any(homeClubDto.getClass()))).thenReturn(homeClubDto);

        String requestJson = objectMapper.writeValueAsString(homeClubDto);
        String responseJson = objectMapper.writeValueAsString(homeClubDto);

        mockMvc.perform(post("/clubs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseJson))
                .andDo(print());

        verify(clubService, times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Save duplicated club Test")
    public void saveDuplicatedClubTeste() throws Exception {

        when(clubService.save(Mockito.any(ClubDto.class))).thenThrow(new RuntimeException(new SQLIntegrityConstraintViolationException("Duplicate entry")));

        String requestJson = objectMapper.writeValueAsString(homeClubDto);

        mockMvc.perform(post("/clubs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict())
                .andDo(print());

        verify(clubService, times(1)).save(Mockito.any(ClubDto.class));
    }

    //TODO CORRIGIR ESSE MÉTODO
    @Test
    @DisplayName("Save club with blank name Test")
    public void saveClubWithBlankNameTest() throws Exception {
        ClubDto invalidClubDto = new ClubDto("");

        doThrow(new RuntimeException(new Exception())).when(clubService).save(invalidClubDto);

        String requestJson = objectMapper.writeValueAsString(invalidClubDto);

        mockMvc.perform(post("/clubs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(clubService, times(1)).save(invalidClubDto);
    }

    @Test
    @DisplayName("Update club Test")
    public void updateClubTest() throws Exception {
        UUID idClub = UUID.randomUUID();

        ClubDto updatedClub = new ClubDto("vasco");

        when(clubService.update(Mockito.any(idClub.getClass()), Mockito.any(updatedClub.getClass()))).thenReturn(updatedClub);

        String requestJson = objectMapper.writeValueAsString(updatedClub);
        String responseJson = objectMapper.writeValueAsString(updatedClub);

        mockMvc.perform(put("/clubs/{id}", idClub)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson))
                .andDo(print());

        verify(clubService, times(1)).update(Mockito.any(idClub.getClass()), Mockito.any(updatedClub.getClass()));
    }

    @Test
    @DisplayName("Delete club Test")
    public void deleteClubTest() throws Exception{
        UUID idClub = UUID.randomUUID();

        when(clubService.delete(idClub)).thenReturn("Club deleted successfully.");

        mockMvc.perform(delete("/clubs/{id}", UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(clubService, times(1)).delete(Mockito.any());
    }

    @Test
    @DisplayName("Delete Not Found Club")
    public void deleteNotFoundClub() throws Exception{
        UUID idClub = UUID.randomUUID();

        when(clubService.delete(idClub)).thenThrow(new EntityNotFoundException("Club not found"));

        mockMvc.perform(delete("/clubs/{id}", idClub))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
