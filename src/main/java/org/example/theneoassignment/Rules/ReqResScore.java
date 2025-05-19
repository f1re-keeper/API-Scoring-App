package org.example.theneoassignment.Rules;

import io.swagger.v3.oas.models.OpenAPI;

public class ReqResScore implements RuleBasis{
    @Override
    public String getName() {
        return "Request/Response Examples (10 pts)";
    }

    @Override
    public int getWeight() {
        return 10;
    }

    @Override
    public double calculateScore(OpenAPI openAPI, StringBuilder feedback) {
        int count = 0;
        int totalOps = 0;

        if (openAPI.getPaths() != null) {
            for (var entry : openAPI.getPaths().entrySet()) {
                for (var op : entry.getValue().readOperations()) {
                    if(op.getResponses() == null) break;
                    totalOps++;
                    boolean hasResponseExample = op.getResponses() != null &&
                            op.getResponses().values().stream().anyMatch(resp ->
                                    resp.getContent() != null &&
                                            resp.getContent().values().stream().anyMatch(media ->
                                                    media.getExamples() != null && !media.getExamples().isEmpty() ||
                                                            media.getExample() != null
                                            )
                            );

                    boolean hasRequestExample = op.getRequestBody() != null &&
                            op.getRequestBody().getContent() != null &&
                            op.getRequestBody().getContent().values().stream().anyMatch(media ->
                                    media.getExamples() != null && !media.getExamples().isEmpty() ||
                                            media.getExample() != null
                            );

                    if (hasRequestExample || hasResponseExample) {
                        count++;
                    }
                }
            }
        }

        if (count == 0) {
            feedback.append("No examples provided in request/response bodies. ");
            return 0;
        } else if (count < getWeight()/2) {
            feedback.append("Only a few examples provided. ");
            return 5;
        } else {
            return (double)count / totalOps * getWeight();
        }
    }
}
