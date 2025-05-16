package org.example.theneoassignment.Rules;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;

import java.util.*;

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
    public int calculateScore(OpenAPI openAPI, StringBuilder feedback) {
        int score = 0;
        if (openAPI.getPaths() == null) return 0;

        for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
            String path = entry.getKey();
            if (!path.startsWith("/")) feedback.append("Path does not start with '/': ").append(path).append("\n");
            else score++;
        }

        return Math.min(getWeight(), score);
    }
}
