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
    public double calculateScore(OpenAPI openAPI, StringBuilder feedback) {
        int validOps = 0;
        int totalOps = 0;
        if (openAPI.getPaths() == null) return 0;

        for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
            for (Operation operation : entry.getValue().readOperations()) {
                totalOps++;
                Map<String, ApiResponse> responses = operation.getResponses();
                if(responses == null) {
                    feedback.append("No response codes: ").append(entry.getKey()).append(".");
                    continue;
                }
                boolean hasUsefulCodes = responses.keySet().stream().anyMatch(code ->
                        code.startsWith("2") || code.startsWith("4") || code.startsWith("5")
                );
                if (responses.isEmpty() || !hasUsefulCodes) {
                    feedback.append("No response codes: ").append(entry.getKey()).append(".");
                } else {
                    validOps++;
                }
            }
        }
        double score = ((validOps / (double) totalOps) * getWeight());
        if(score!=getWeight()) feedback.append("Lacking useful codes.");
        return score;
    }
}
