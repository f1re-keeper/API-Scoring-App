package org.example.theneoassignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.example.theneoassignment.Controller.ScoreController;
import org.example.theneoassignment.Model.*;
import org.example.theneoassignment.Service.ScoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String loadAPI() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("unit_tests/max-schemas.json");
        assertNotNull(is);
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    @Test
    void validSpecTest() throws Exception {
        Result mockResult = new Result();
        mockResult.setScore(95);

        Report mockReport = new Report();
        mockReport.setGrade("A");
        mockReport.setResult(mockResult);

        when(scoreService.score(any(OpenAPI.class))).thenReturn(mockReport);
        String content = loadAPI();
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
