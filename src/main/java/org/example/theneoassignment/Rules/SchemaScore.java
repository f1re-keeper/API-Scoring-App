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
    public int calculateScore(OpenAPI openAPI, StringBuilder feedback) {
        if (openAPI.getComponents() == null || openAPI.getComponents().getSchemas() == null) {
            feedback.append("Schemas are not defined.\n");
            return 0;
        }

        int valid = 0;
        for (Map.Entry<String, Schema> entry : openAPI.getComponents().getSchemas().entrySet()) {
            Schema schema = entry.getValue();
            if (schema.getType() == null) {
                feedback.append("Schema `" + entry.getKey() + "` is missing a type.\n");
            } else {
                valid++;
            }
        }

        return Math.min(getWeight(), valid);
    }
}
