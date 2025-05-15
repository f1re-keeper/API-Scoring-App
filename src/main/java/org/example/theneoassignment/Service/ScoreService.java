package org.example.theneoassignment.Service;

import io.swagger.v3.oas.models.OpenAPI;
import org.example.theneoassignment.Model.Report;
import org.example.theneoassignment.Rules.DescriptionRule;
import org.example.theneoassignment.Rules.Rule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreService {
    public Report score(OpenAPI spec) {
        List<Rule> rules = List.of(
                new DescriptionRule(spec)
                // Other rules
        );

        Report report = new Report();
        for (Rule rule : rules) {
            report.add(rule.eval());
        }
        return report;
    }
}
