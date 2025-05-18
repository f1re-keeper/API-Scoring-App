package org.example.theneoassignment.Rules;

import io.swagger.v3.oas.models.OpenAPI;

public class DescriptionScore implements RuleBasis {
    @Override
    public String getName() {
        return "Descriptions & Documentation";
    }

    @Override
    public int getWeight() {
        return 20;
    }

    @Override
    public double calculateScore(OpenAPI openAPI, StringBuilder feedback) {
        int issues = 0;
        if (openAPI.getPaths() != null) {
            for (var entry : openAPI.getPaths().entrySet()) {
                var pathItem = entry.getValue();
                if (pathItem.readOperations() != null) {
                    for (var op : pathItem.readOperations()) {
                        if (op.getDescription() == null || op.getDescription().isBlank()) {
                            issues++;
                        }
                    }
                }
            }
        }
        int score = Math.max(0, getWeight() - issues * 2);
        if(score != getWeight()) feedback.append("Missing some descriptions.");
        return Math.min(getWeight(), score);
    }
}
