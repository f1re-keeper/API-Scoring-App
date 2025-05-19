package org.example.theneoassignment;

import io.swagger.v3.oas.models.OpenAPI;
import org.example.theneoassignment.Controller.ScoreController;
import org.example.theneoassignment.Model.*;
import org.example.theneoassignment.Service.ScoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ScoreController.class)
public class TestScoreController {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScoreService scoreService;

    @Test
    void validSpecTest() throws Exception {
        Result mockResult = new Result();
        mockResult.setScore(95);

        Report mockReport = new Report();
        mockReport.setGrade("A");
        mockReport.setResult(mockResult);

        when(scoreService.score(any(OpenAPI.class))).thenReturn(mockReport);
        String content = Files.readString(Paths.get("src/test/resources/unit_tests/max-schemas.json"));
        mockMvc.perform(post("/api/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value("A"));
    }

    @Test
    void badRequestTest() throws Exception {
        mockMvc.perform(post("/api/score")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }
}
