package org.example.theneoassignment.Rules;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;

import java.util.Map;

public class ReqResScore implements RuleBasis{
    @Override
    public String getName() {
        return "Request/Response Examples";
    }

    @Override
    public int getWeight() {
        return 10;
    }

    @Override
    public int calculateScore(OpenAPI openAPI, StringBuilder feedback) {
        int exampleCount = 0;

        if (openAPI.getPaths() == null) return 0;

        for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
            for (Operation op : entry.getValue().readOperations()) {
                boolean hasExample = op.getResponses().values().stream().anyMatch(resp ->
                        resp.getContent() != null && resp.getContent().values().stream().anyMatch(media ->
                                media.getExamples() != null || media.getExample() != null
                        )
                );
                if (hasExample) exampleCount++;
                else feedback.append("No examples in ").append(entry.getKey()).append("\n");
            }
        }

        return Math.min(getWeight(), exampleCount);
    }
}
