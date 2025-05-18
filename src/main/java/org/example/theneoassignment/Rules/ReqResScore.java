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
    public double calculateScore(OpenAPI openAPI, StringBuilder feedback) {
        int count = 0;

        if (openAPI.getPaths() != null) {
            for (var entry : openAPI.getPaths().entrySet()) {
                for (var op : entry.getValue().readOperations()) {
                    boolean hasExample =
                            (op.getRequestBody() != null && op.getRequestBody().getContent() != null &&
                                    op.getRequestBody().getContent().values().stream().anyMatch(media ->
                                            media.getExamples() != null || media.getExample() != null)) ||
                                    op.getResponses().values().stream().anyMatch(resp ->
                                            resp.getContent() != null && resp.getContent().values().stream().anyMatch(media ->
                                                    media.getExamples() != null || media.getExample() != null));

                    if (hasExample) count++;
                }
            }
        }

        if (count < 1) {
            feedback.append("No examples provided in request/response bodies.\n");
            return 0;
        } else if (count < getWeight()/2) {
            feedback.append("Only a few examples provided.\n");
            return 5;
        } else {
            return getWeight();
        }
    }
}
