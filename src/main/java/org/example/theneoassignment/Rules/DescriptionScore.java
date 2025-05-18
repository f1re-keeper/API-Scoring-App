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
        int totalOps = 0;
        int documentedOps = 0;

        if (openAPI.getPaths() != null) {
            for (var entry : openAPI.getPaths().entrySet()) {
                var pathItem = entry.getValue();
                if (pathItem.readOperations() != null) {
                    for (var op : pathItem.readOperations()) {
                        totalOps++;
                        if (op.getDescription() != null && !op.getDescription().isBlank()) {
                            documentedOps++;
                        } else if (op.getSummary() != null && !op.getSummary().isBlank()) {
                            documentedOps++;
                        }
                    }
                }
            }
        }

        if (totalOps == 0) {
            feedback.append("No operations to document.");
            return 0;
        }

        double ratio = (double) documentedOps / totalOps;
        double score = Math.round(ratio * getWeight());

        if (score < getWeight()) {
            feedback.append("Missing some descriptions.");
        }

        return score;
    }

}
