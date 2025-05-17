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

    public int getTotalScore() {
        return result.getScore();
    }

    public void calculateGrade() {
        double pct = (double) getTotalScore() / 100;
        grade = pct >= 90 ? "A" :
                pct >= 80 ? "B" :
                        pct >= 70 ? "C" :
                                pct >= 60 ? "D" : "F";
    }
}
