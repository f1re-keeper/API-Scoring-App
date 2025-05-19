package org.example.theneoassignment;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.example.theneoassignment.Model.Report;
import org.example.theneoassignment.Model.Result;
import org.example.theneoassignment.Service.ScoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTests {
    private final ScoreService scorer = new ScoreService();
    private OpenAPI loadOpenApi(String name) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("unit_tests/" + name);
        assertNotNull(is);
        String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        return new OpenAPIV3Parser().readContents(content, null, null).getOpenAPI();
    }

    @Test
    @DisplayName("Valid CRUD naming should get full score")
    void testGoodCrudNaming() throws IOException {
        OpenAPI api = loadOpenApi("good-crud-naming.json");
        Report report = scorer.score(api);
        Result result = report.getResult();
        assertEquals(15.0, result.getRuleBreakdown().get("Paths & Operations (15 pts)"));
        assertFalse(result.getFeedback().contains("does not follow CRUD naming"));
    }

    @Test
    @DisplayName("Invalid CRUD naming should reduce score")
    void testBadCrudNaming() throws IOException {
        OpenAPI api = loadOpenApi("bad-crud-naming.json");
        Report report = scorer.score(api);
        Result result = report.getResult();
        assertTrue(result.getRuleBreakdown().get("Paths & Operations (15 pts)") < 15.0);
        assertTrue(result.getFeedback().contains("does not follow CRUD naming"));
    }

    @Test
    @DisplayName("Bad pathes should reduce the score")
    void testBasPathes() throws IOException {
        OpenAPI api = loadOpenApi("bad-pathes.json");
        Report report = scorer.score(api);
        Result result = report.getResult();
        String feedback = result.getFeedback();
        assertTrue(result.getRuleBreakdown().get("Paths & Operations (15 pts)") < 15.0);
        assertTrue(feedback.contains("Path does not start with '/'"));
        assertTrue(feedback.contains("No operations defined for path: /no-op"));
        assertTrue(feedback.contains("Duplicate operation ID: dupOpId"));
    }

    @Test
    @DisplayName("Missing security should get zero score")
    void testNoSecurity() throws IOException {
        OpenAPI api = loadOpenApi("no-security.json");
        Report report = scorer.score(api);
        Result result = report.getResult();
        assertEquals(0.0, result.getRuleBreakdown().get("Security Schemes (10 pts)"));
        assertTrue(result.getFeedback().contains("No security schemes"));
    }

    @Test
    @DisplayName("Full security should get max score")
    void testMaxSecurity() throws IOException {
        OpenAPI api = loadOpenApi("max-security.json");
        Report report = scorer.score(api);
        Result result = report.getResult();
        assertEquals(10.0, result.getRuleBreakdown().get("Security Schemes (10 pts)"));
        assertFalse(result.getFeedback().contains("No security schemes"));
    }

    @Test
    @DisplayName("Having some security should get reduced score but not zero")
    void testSomeSecurity() throws IOException {
        OpenAPI api = loadOpenApi("some-security.json");
        Report report = scorer.score(api);
        Result result = report.getResult();
        assertTrue(result.getRuleBreakdown().get("Security Schemes (10 pts)") < 10.0);
        assertFalse(result.getFeedback().contains("No security schemes"));
        assertTrue(result.getFeedback().contains("Security schemes defined but not applied to any operations."));
    }

    @Test
    @DisplayName("Missing descriptions should get partial or zero score")
    void testMissingDescriptions() throws IOException{
        OpenAPI api = loadOpenApi("missing-descriptions.json");
        Report report = scorer.score(api);
        Result result = report.getResult();
        assertTrue(result.getRuleBreakdown().get("Descriptions & Documentation (20 pts)") < 20.0);
        assertTrue(result.getFeedback().contains("Missing some descriptions"));
    }

    @Test
    @DisplayName("Having perfect descriptions should get max score")
    void testMaxDescriptions() throws IOException{
        OpenAPI api = loadOpenApi("max-descriptions.json");
        Report report = scorer.score(api);
        Result result = report.getResult();
        assertEquals(result.getRuleBreakdown().get("Descriptions & Documentation (20 pts)"),  20.0);
        assertFalse(result.getFeedback().contains("Missing some descriptions"));
    }

    @Test
    @DisplayName("Having perfect schemas should get max score")
    void testMaxSchemas() throws IOException{
        OpenAPI api = loadOpenApi("max-schemas.json");
        Report report = scorer.score(api);
        Result result = report.getResult();
        assertEquals(result.getRuleBreakdown().get("Schema & Types (20 pts)"),  20.0);
        assertFalse(result.getFeedback().contains("No schemas found in components or inline."));
    }

    @Test
    @DisplayName("Having invalid schemas should get reduced score")
    void testInvalidSchema() throws IOException{
        OpenAPI api = loadOpenApi("max-misc.json");
        Report report = scorer.score(api);
        Result result = report.getResult();
        assertTrue(result.getRuleBreakdown().get("Schema & Types (20 pts)") < 20.0);
        assertTrue(result.getFeedback().contains("Some schemas are incomplete or missing."));
    }

    @Test
    @DisplayName("Having no schemas at all should get 0 score")
    void testNoSchemas() throws IOException{
        OpenAPI api = loadOpenApi("no-schema.json");
        Report report = scorer.score(api);
        Result result = report.getResult();
        assertEquals(result.getRuleBreakdown().get("Schema & Types (20 pts)"), 0);
        assertTrue(result.getFeedback().contains("No schemas found in components or inline."));
    }

    @Test
    @DisplayName("Having no response codes at all should get 0 score")
    void testNoResponseCodes() throws IOException{
        OpenAPI api = loadOpenApi("no-response-codes.json");
        Report report = scorer.score(api);
        Result result = report.getResult();
        assertEquals(result.getRuleBreakdown().get("Response Codes (15 pts)"), 0);
        assertTrue(result.getFeedback().contains("No response codes"));
    }

    @Test
    @DisplayName("Having response codes should not get 0 score")
    void testPresentResponseCodes() throws IOException{
        OpenAPI api = loadOpenApi("max-schemas.json");
        Report report = scorer.score(api);
        Result result = report.getResult();
        assertTrue(result.getRuleBreakdown().get("Response Codes (15 pts)") > 0);
        assertFalse(result.getFeedback().contains("No response codes"));
    }

    @Test
    @DisplayName("OpenAPI with examples should not have 0 score in Request/Response Examples category")
    void testPresentExamples() throws IOException{
        OpenAPI api = loadOpenApi("with-examples.json");
        Report report = scorer.score(api);
        Result result = report.getResult();
        assertTrue(0 < result.getRuleBreakdown().get("Request/Response Examples (10 pts)"));
        assertFalse(result.getFeedback().contains("No examples provided in request/response bodies."));
    }

    @Test
    @DisplayName("OpenAPI with no examples should have 0 score in Request/Response Examples category")
    void testNoExamples() throws IOException{
        OpenAPI api = loadOpenApi("bad-misc.json");
        Report report = scorer.score(api);
        Result result = report.getResult();
        assertEquals(0, result.getRuleBreakdown().get("Request/Response Examples (10 pts)"));
        assertTrue(result.getFeedback().contains("No examples provided in request/response bodies."));
    }

    @Test
    @DisplayName("Having all of the best miscellaneous practices should get max score")
    void testMaxMisc() throws IOException{
        OpenAPI api = loadOpenApi("max-misc.json");
        Report report = scorer.score(api);
        Result result = report.getResult();
        assertEquals(result.getRuleBreakdown().get("Miscellaneous Best Practices (10 pts)"), 10.0);
    }

    @Test
    @DisplayName("Having none of the best miscellaneous practices should get 0 score")
    void testBadMisc() throws IOException{
        OpenAPI api = loadOpenApi("bad-misc.json");
        Report report = scorer.score(api);
        Result result = report.getResult();
        String feedback = result.getFeedback();
        assertEquals(result.getRuleBreakdown().get("Miscellaneous Best Practices (10 pts)"), 0);
        assertTrue(feedback.contains("API version not found."));
        assertTrue(feedback.contains("Servers array not found."));
        assertTrue(feedback.contains("Tags not found."));
        assertTrue(feedback.contains("Reusable components not found."));
    }
}
