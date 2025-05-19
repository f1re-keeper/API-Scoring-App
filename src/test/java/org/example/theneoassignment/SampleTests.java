package org.example.theneoassignment;

import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import io.swagger.v3.oas.models.OpenAPI;
import org.example.theneoassignment.Model.Report;
import org.example.theneoassignment.Model.Result;
import org.example.theneoassignment.Service.ScoreService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Paths;

public class SampleTests {
    private final ScoreService scoreService = new ScoreService();

    @Test
    public void testTicketmasterSpecScoring() throws Exception {
        String ticketmaster = Files.readString(Paths.get("src/test/resources/samples/ticketmaster.json"));

        SwaggerParseResult parseResult = new OpenAPIV3Parser().readContents(ticketmaster, null, null);
        OpenAPI openAPI = parseResult.getOpenAPI();

        assertNotNull(openAPI, "Parsed OpenAPI object should not be null");

        Report report = scoreService.score(openAPI);
        Result result = report.getResult();
        String feedback = result.getFeedback();

        assertNotNull(result);
        assertTrue(result.getScore() > 50, "This spec should not have an F grade");
        assertFalse(feedback.isEmpty(), "Feedback should not be empty");
        assertFalse(feedback.contains("Missing some descriptions"));
        assertFalse(feedback.contains("No schemas found in components or inline."));
        assertFalse(feedback.contains("API version not found."));
        assertFalse(feedback.contains("Servers array not found."));
        assertFalse(feedback.contains("Reusable components not found."));
    }

    @Test
    public void testVimeoSpecScoring() throws Exception {
        String ticketmaster = Files.readString(Paths.get("src/test/resources/samples/vimeo.json"));

        SwaggerParseResult parseResult = new OpenAPIV3Parser().readContents(ticketmaster, null, null);
        OpenAPI openAPI = parseResult.getOpenAPI();

        assertNotNull(openAPI, "Parsed OpenAPI object should not be null");

        Report report = scoreService.score(openAPI);
        Result result = report.getResult();
        String feedback = result.getFeedback();

        assertNotNull(result);
        assertTrue(result.getScore() > 50, "This spec should not have an F grade");
        assertFalse(feedback.isEmpty(), "Feedback should not be empty");
        assertFalse(feedback.contains("No security schemes"));
        assertFalse(feedback.contains("Missing some descriptions"));
        assertFalse(feedback.contains("No schemas found in components or inline."));
        assertFalse(feedback.contains("No response codes"));
        assertTrue(feedback.contains("No examples provided in request/response bodies."));
        assertFalse(feedback.contains("API version not found."));
        assertFalse(feedback.contains("Servers array not found."));
        assertFalse(feedback.contains("Tags not found."));
        assertFalse(feedback.contains("Reusable components not found."));
    }

    @Test
    public void testSlackSpecScoring() throws Exception {
        String ticketmaster = Files.readString(Paths.get("src/test/resources/samples/slack.json"));

        SwaggerParseResult parseResult = new OpenAPIV3Parser().readContents(ticketmaster, null, null);
        OpenAPI openAPI = parseResult.getOpenAPI();

        assertNotNull(openAPI, "Parsed OpenAPI object should not be null");

        Report report = scoreService.score(openAPI);
        Result result = report.getResult();
        String feedback = result.getFeedback();

        assertNotNull(result);
        assertTrue(result.getScore() > 50, "This spec should not have an F grade");
        assertFalse(feedback.isEmpty(), "Feedback should not be empty");
        assertTrue(feedback.contains("No security schemes"));
        assertFalse(feedback.contains("Missing some descriptions"));
        assertFalse(feedback.contains("No schemas found in components or inline."));
        assertFalse(feedback.contains("No response codes"));
        assertTrue(feedback.contains("No examples provided in request/response bodies."));
        assertFalse(feedback.contains("API version not found."));
        assertFalse(feedback.contains("Servers array not found."));
        assertTrue(feedback.contains("Tags not found."));
        assertFalse(feedback.contains("Reusable components not found."));
    }
}
