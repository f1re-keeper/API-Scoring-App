package org.example.theneoassignment.Rules;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;

import java.util.*;

public class SchemaScore implements RuleBasis{
    @Override
    public String getName() {
        return "Schema & Types";
    }

    @Override
    public int getWeight() {
        return 20;
    }

    @Override
    public double calculateScore(OpenAPI openAPI, StringBuilder feedback) {
        if (openAPI.getComponents() == null || openAPI.getComponents().getSchemas() == null) {
            feedback.append("Schemas are not defined.\n");
            return 0;
        }

        int valid = 0;
        for (Map.Entry<String, Schema> entry : openAPI.getComponents().getSchemas().entrySet()) {
            Schema schema = entry.getValue();
            if (schema.getType() != null || schema.get$ref() != null || (schema.getProperties() != null && !schema.getProperties().isEmpty())) {
                valid++;
            } else {
                feedback.append("Schema `").append(entry.getKey()).append("` is incomplete or missing type.");
            }
        }

        if(valid != getWeight()) feedback.append("Missing some Schemas.");
        return (int) ((valid / (double) openAPI.getComponents().getSchemas().size()) * getWeight());
    }
}
