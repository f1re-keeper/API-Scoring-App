package org.example.theneoassignment.Rules;

import io.swagger.v3.oas.models.OpenAPI;
import org.example.theneoassignment.Model.Result;

public class DescriptionRule extends Rule {
    public DescriptionRule(OpenAPI spec) {
        super(spec);
    }

    @Override
    public Result eval() {
        int issues = 0;
        if (spec.getPaths() != null) {
            for (var entry : spec.getPaths().entrySet()) {
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
        int score = Math.max(0, 20 - issues * 2);
        return new Result("DescriptionRule", 20, score, issues);
    }
}
