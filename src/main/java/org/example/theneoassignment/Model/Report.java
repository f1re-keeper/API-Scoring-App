package org.example.theneoassignment.Model;

import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Report {
    private Result result;
    private String grade = "";

    public double getTotalScore() {
        return result.getScore();
    }

    public void calculateGrade() {
        double pct = getTotalScore();
        grade = pct >= 90 ? "A" :
                pct >= 80 ? "B" :
                        pct >= 70 ? "C" :
                                pct >= 60 ? "D" : "F";
    }
}
