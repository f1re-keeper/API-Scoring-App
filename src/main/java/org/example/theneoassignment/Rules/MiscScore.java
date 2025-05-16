package org.example.theneoassignment.Rules;

import io.swagger.v3.oas.models.OpenAPI;

public class MiscScore implements RuleBasis{
    @Override
    public String getName() {
        return "Miscellaneous Best Practices";
    }

    @Override
    public int getWeight() {
        return 10;
    }

    @Override
    public int calculateScore(OpenAPI openAPI, StringBuilder feedback) {
        int score = 0;
        if (openAPI.getInfo() != null && openAPI.getInfo().getVersion() != null) score += 2;
        else feedback.append("API version not found.\n");

        if (openAPI.getServers() != null && !openAPI.getServers().isEmpty()) score += 2;
        else feedback.append("Servers array not found.\n");

        if (openAPI.getTags() != null && !openAPI.getTags().isEmpty()) score += 2;
        else feedback.append("Tags not found.\n");

        if (openAPI.getComponents() != null && openAPI.getComponents().getSchemas() != null) score += 2;
        else feedback.append("Reusable components not found.\n");

        return Math.min(getWeight(), score);
    }
}
