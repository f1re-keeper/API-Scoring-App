package org.example.theneoassignment.Rules;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SchemaScore implements RuleBasis{
    @Override
    public String getName() {
        return "Schema & Types (20 pts)";
    }

    @Override
    public int getWeight() {
        return 20;
    }

    @Override
    public double calculateScore(OpenAPI openAPI, StringBuilder feedback) {
        AtomicInteger validSchemas = new AtomicInteger(0);
        AtomicInteger totalSchemas = new AtomicInteger(0);

        componentSchemaScore(openAPI, feedback, validSchemas, totalSchemas);

        if (openAPI.getPaths() != null) {
            for (PathItem pathItem : openAPI.getPaths().values()) {
                for (Operation op : pathItem.readOperations()) {
                    responseSchemas(op, totalSchemas, validSchemas);
                    requestSchemas(op, totalSchemas, validSchemas);
                    parameterSchemas(op, totalSchemas, validSchemas);
                }
            }
        }

        if (totalSchemas.get() == 0) {
            feedback.append("No schemas found in components or inline. ");
            return 0;
        }

        double ratio = (double) validSchemas.get() / totalSchemas.get();
        double score = ratio * getWeight();
        if (score != getWeight()) feedback.append("Some schemas are incomplete or missing. ");
        return score;
    }

    private void componentSchemaScore(OpenAPI openAPI, StringBuilder feedback,
                                      AtomicInteger validSchemas, AtomicInteger totalSchemas){
        if (openAPI.getComponents() != null && openAPI.getComponents().getSchemas() != null) {
            for (Map.Entry<String, Schema> entry : openAPI.getComponents().getSchemas().entrySet()) {
                Schema schema = entry.getValue();
                totalSchemas.incrementAndGet();
                if (isValidSchema(schema)) {
                    validSchemas.incrementAndGet();
                } else {
                    feedback.append("Component schema `").append(entry.getKey()).append("` is incomplete. ");
                }
            }
        }
    }

    private void responseSchemas(Operation op,
                                 AtomicInteger totalSchemas, AtomicInteger validSchemas){
        if (op.getResponses() != null) {
            for (ApiResponse resp : op.getResponses().values()) {
                if (resp.getContent() != null) {
                    for (MediaType media : resp.getContent().values()) {
                        Schema schema = media.getSchema();
                        totalSchemas.incrementAndGet();
                        if (schema != null) {
                            if (isValidSchema(schema)) {
                                validSchemas.incrementAndGet();
                            }
                        }
                    }
                }
            }
        }
    }

    private void requestSchemas(Operation op, AtomicInteger totalSchemas, AtomicInteger validSchemas){
        if (op.getRequestBody() != null && op.getRequestBody().getContent() != null) {
            for (MediaType media : op.getRequestBody().getContent().values()) {
                Schema schema = media.getSchema();
                totalSchemas.incrementAndGet();

                if (schema != null) {
                    if (isValidSchema(schema)) {
                        validSchemas.incrementAndGet();
                    }
                }
            }
        }
    }

    private void parameterSchemas(Operation op, AtomicInteger totalSchemas, AtomicInteger validSchemas){
        if (op.getParameters() != null) {
            for (Parameter param : op.getParameters()) {
                Schema schema = param.getSchema();
                totalSchemas.incrementAndGet();
                if (schema != null) {
                    if (isValidSchema(schema)) {
                        validSchemas.incrementAndGet();
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
