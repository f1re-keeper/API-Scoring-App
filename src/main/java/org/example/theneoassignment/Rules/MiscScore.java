package org.example.theneoassignment.Rules;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;

import java.util.List;

public class MiscScore implements RuleBasis{
    @Override
    public String getName() {
        return "Miscellaneous Best Practices (10 pts)";
    }

    @Override
    public int getWeight() {
        return 10;
    }

    @Override
    public double calculateScore(OpenAPI openAPI, StringBuilder feedback) {
        double score = 0;
        if (openAPI.getInfo() != null && openAPI.getInfo().getVersion() != null &&
                !openAPI.getInfo().getVersion().isBlank()) score += 2.5;
        else feedback.append("API version not found. ");

        List<Server> servers = openAPI.getServers();
        if (servers != null &&
                !servers.isEmpty() && !servers.get(0).getUrl().equals("/")) score += 2.5;
        else feedback.append("Servers array not found. ");

        if (openAPI.getTags() != null && !openAPI.getTags().isEmpty()) score += 2.5;
        else feedback.append("Tags not found. ");

        if (openAPI.getComponents() != null && openAPI.getComponents().getSchemas() != null) score += 2.5;
        else feedback.append("Reusable components not found. ");

        return Math.min(getWeight(), score);
    }
}
