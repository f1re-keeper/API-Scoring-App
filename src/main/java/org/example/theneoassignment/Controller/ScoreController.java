package org.example.theneoassignment.Controller;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.example.theneoassignment.Model.Report;
import org.example.theneoassignment.Model.Result;
import org.example.theneoassignment.Service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/score")
public class ScoreController {
    @Autowired
    ScoreService scoreService;

    @PostMapping
    public Report scoreSpec(@RequestBody String openApiSpec) {
        SwaggerParseResult parseResult = new OpenAPIV3Parser().readContents(openApiSpec, null, null);
        OpenAPI openAPI = parseResult.getOpenAPI();

        if (openAPI == null) {
            throw new IllegalArgumentException("Invalid OpenAPI specification.");
        }

        return scoreService.score(openAPI);
    }
}
