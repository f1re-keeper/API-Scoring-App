package org.example.theneoassignment.Controller;

import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.example.theneoassignment.Model.Report;
import org.example.theneoassignment.Service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/score")
public class ScoreController {
    @Autowired
    ScoreService scoreService;

    @PostMapping
    public Report scoreSpec(@RequestBody String openApiSpec) {
        SwaggerParseResult result = new OpenAPIV3Parser().readContents(openApiSpec, null, null);
        if (result.getMessages() != null && !result.getMessages().isEmpty()) {
            throw new IllegalArgumentException("Invalid OpenAPI Spec:\n" + result.getMessages());
        }
        return scoreService.score(result.getOpenAPI());
    }
}
