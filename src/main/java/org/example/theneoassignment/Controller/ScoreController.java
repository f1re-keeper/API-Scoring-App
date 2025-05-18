package org.example.theneoassignment.Controller;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.example.theneoassignment.Model.Report;
import org.example.theneoassignment.Service.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/score")
public class ScoreController {
    private static final Logger logger = LoggerFactory.getLogger(ScoreController.class);

    @Autowired
    ScoreService scoreService;

    @PostMapping
    public Report scoreSpec(@RequestBody String openApiSpec) {
        if (openApiSpec == null || openApiSpec.trim().isEmpty()) {
            logger.info("Bad request body.");
            throw new IllegalArgumentException("Request body cannot be empty");
        }

        logger.info("Parsing OpenAPI spec");
        SwaggerParseResult parseResult = new OpenAPIV3Parser().readContents(openApiSpec, null, null);
        OpenAPI openAPI = parseResult.getOpenAPI();

        if (openAPI == null) {
            throw new IllegalArgumentException("Invalid OpenAPI specification.");
        }

        logger.info("Grading OpenAPI spec");
        return scoreService.score(openAPI);
    }
}
