package org.example.theneoassignment.Rules;

import io.swagger.v3.oas.models.OpenAPI;

public class SecurityScore implements RuleBasis{
    @Override
    public String getName() {
        return "Security Schemes";
    }

    @Override
    public int getWeight() {
        return 10;
    }

    @Override
    public double calculateScore(OpenAPI openAPI, StringBuilder feedback) {
        if (openAPI.getComponents() != null && openAPI.getComponents().getSecuritySchemes() != null
                && !openAPI.getComponents().getSecuritySchemes().isEmpty()) {
            return getWeight();
        }
        feedback.append("No security schemes defined.");
        return 0;
    }
}
