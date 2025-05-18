package org.example.theneoassignment.Rules;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;

import java.util.*;
import java.util.regex.Pattern;

public class PathsScore implements RuleBasis{
    @Override
    public String getName() {
        return "Paths & Operations";
    }

    @Override
    public int getWeight() {
        return 15;
    }

    @Override
    public double calculateScore(OpenAPI openAPI, StringBuilder feedback) {
        if (openAPI.getPaths() == null) {
            feedback.append("No paths defined.\n");
            return 0;
        }

        int score = 0;
        Set<String> operationIds = new HashSet<>();
        Pattern crudPattern = Pattern.compile("^(get|list|create|update|delete).+", Pattern.CASE_INSENSITIVE);

        for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
            String path = entry.getKey();
            if (!path.startsWith("/")) {
                feedback.append("Path does not start with '/': ").append(path).append("\n");
                continue;
            }

            PathItem item = entry.getValue();
            List<Operation> operations = item.readOperations();
            if (operations == null || operations.isEmpty()) {
                feedback.append("No operations defined for path: ").append(path).append("\n");
                continue;
            }

            for (Operation op : operations) {
                String opId = op.getOperationId();
                if (opId == null || !crudPattern.matcher(opId).matches()) {
                    feedback.append("Operation ID does not follow CRUD naming: ").append(path).append("\n");
                } else if (!operationIds.add(opId)) {
                    feedback.append("Duplicate operation ID: ").append(opId).append("\n");
                } else {
                    score++;
                }
            }
        }

        if (score < getWeight()/3) feedback.append("Too few valid operations defined.\n");
        return Math.min(getWeight(), score);
    }
}
