package org.example.theneoassignment.Rules;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;

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
        int validSchemas = 0;
        int totalSchemas = 0;

        componentSchemaScore(openAPI, feedback, validSchemas, totalSchemas);

        if (openAPI.getPaths() != null) {
            for (PathItem pathItem : openAPI.getPaths().values()) {
                for (Operation op : pathItem.readOperations()) {
                    responseSchemas(openAPI, feedback, op, totalSchemas, validSchemas);
                    requestSchemas(openAPI, feedback, op, totalSchemas, validSchemas);
                    parameterSchemas(openAPI, feedback, op, totalSchemas, validSchemas);
                }
            }
        }

        if (totalSchemas == 0) {
            feedback.append("No schemas found in components or inline.");
            return 0;
        }

        double ratio = (double) validSchemas / totalSchemas;
        double score = Math.round(ratio * getWeight());
        if (score != getWeight()) feedback.append("Some schemas are incomplete or missing.");
        return score;
    }

    private void componentSchemaScore(OpenAPI openAPI, StringBuilder feedback,
                                      int validSchemas, int totalSchemas){
        if (openAPI.getComponents() != null && openAPI.getComponents().getSchemas() != null) {
            for (Map.Entry<String, Schema> entry : openAPI.getComponents().getSchemas().entrySet()) {
                Schema schema = entry.getValue();
                totalSchemas++;
                if (isValidSchema(schema)) {
                    validSchemas++;
                } else {
                    feedback.append("Component schema `").append(entry.getKey()).append("` is incomplete.");
                }
            }
        }
    }

    private void responseSchemas(OpenAPI openAPI, StringBuilder feedback, Operation op,
                                 int totalSchemas, int validSchemas){
        if (op.getResponses() != null) {
            for (ApiResponse resp : op.getResponses().values()) {
                if (resp.getContent() != null) {
                    for (MediaType media : resp.getContent().values()) {
                        Schema schema = media.getSchema();
                        if (schema != null) {
                            totalSchemas++;
                            if (isValidSchema(schema)) {
                                validSchemas++;
                            }
                        }
                    }
                }
            }
        }
    }

    private void requestSchemas(OpenAPI openAPI, StringBuilder feedback, Operation op,
                                int totalSchemas, int validSchemas){
        if (op.getRequestBody() != null && op.getRequestBody().getContent() != null) {
            for (MediaType media : op.getRequestBody().getContent().values()) {
                Schema schema = media.getSchema();
                if (schema != null) {
                    totalSchemas++;
                    if (isValidSchema(schema)) {
                        validSchemas++;
                    }
                }
            }
        }
    }

    private void parameterSchemas(OpenAPI openAPI, StringBuilder feedback, Operation op,
                                  int totalSchemas, int validSchemas){
        if (op.getParameters() != null) {
            for (Parameter param : op.getParameters()) {
                Schema schema = param.getSchema();
                if (schema != null) {
                    totalSchemas++;
                    if (isValidSchema(schema)) {
                        validSchemas++;
                    }
                }
            }
        }
    }

    private boolean isValidSchema(Schema schema) {
        return schema.get$ref() != null ||
                schema.getType() != null ||
                (schema.getProperties() != null && !schema.getProperties().isEmpty());
    }
}
