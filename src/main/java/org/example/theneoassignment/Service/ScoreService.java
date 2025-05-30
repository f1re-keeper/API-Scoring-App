package org.example.theneoassignment.Service;

import io.swagger.v3.oas.models.OpenAPI;
import org.example.theneoassignment.Model.Report;
import org.example.theneoassignment.Model.Result;
import org.example.theneoassignment.Rules.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScoreService {
    Map<String, Double> details = new LinkedHashMap<>();
    private final List<RuleBasis> rules = List.of(
            new SchemaScore(),
            new DescriptionScore(),
            new ResponseCodeScore(),
            new SecurityScore(),
            new ReqResScore(),
            new PathsScore(),
            new MiscScore()
    );

    public Report score(OpenAPI openAPI) {
        StringBuilder feedback = new StringBuilder();
        double totalScore = 0;
        Map<String, Double> details = new LinkedHashMap<>();
        Report report = new Report();

        for (RuleBasis rule : rules) {
            double ruleScore = rule.calculateScore(openAPI, feedback);
            totalScore += ruleScore;
            details.put(rule.getName(), ruleScore);
        }

        Result res = new Result(totalScore, details, feedback.toString());
        report.setResult(res);
        report.calculateGrade();
        return report;
    }
}
