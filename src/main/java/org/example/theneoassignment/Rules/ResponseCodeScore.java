package org.example.theneoassignment.Rules;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.responses.ApiResponse;

import java.util.*;

public class ResponseCodeScore implements RuleBasis{
    @Override
    public String getName() {
        return "Response Codes";
    }

    @Override
    public int getWeight() {
        return 15;
    }

    @Override
    public int calculateScore(OpenAPI openAPI, StringBuilder feedback) {
        int validOps = 0;
        if (openAPI.getPaths() == null) return 0;

        for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
            for (Operation operation : entry.getValue().readOperations()) {
                Map<String, ApiResponse> responses = operation.getResponses();
                if (responses == null || !responses.containsKey("200")) {
                    feedback.append("No 200 response for operation: ").append(entry.getKey()).append("\n");
                } else {
                    validOps++;
                }
            }
        }

        return Math.min(getWeight(), validOps);
    }
}
