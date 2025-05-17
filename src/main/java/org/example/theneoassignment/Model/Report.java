package org.example.theneoassignment.Model;

import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Report {
    private List<Result> results = new ArrayList<>();
    private String grade = "";

    public void add(Result result) {
        results.add(result);
    }

    public int getTotalScore() {
        int totalScore = 0;
        for (Result result : results) {
            int score = result.getScore();
            totalScore += score;
        }
        return totalScore;
    }

    public int getMaxScore() {
        List<Integer> scores = new ArrayList<>();
        for (Result result : results) {
            scores.add(result.getScore());
        }
        return Collections.max(scores);
    }

    public void getGrade() {
        double pct = (double) getTotalScore() / getMaxScore() * 100;
        grade = pct >= 90 ? "A" :
                pct >= 80 ? "B" :
                        pct >= 70 ? "C" :
                                pct >= 60 ? "D" : "F";
    }
}
