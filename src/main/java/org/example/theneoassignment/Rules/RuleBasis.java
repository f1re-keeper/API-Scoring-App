package org.example.theneoassignment.Rules;

import io.swagger.v3.oas.models.OpenAPI;

public interface RuleBasis {
    String getName();

    int getWeight();

    double calculateScore(OpenAPI openAPI, StringBuilder feedback);
}
