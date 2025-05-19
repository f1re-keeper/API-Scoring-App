package org.example.theneoassignment.Model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Report {
    private String grade = "";
    private Result result;

    public void calculateGrade() {
        double pct = result.getScore();
        grade = pct >= 90 ? "A" :
                pct >= 80 ? "B" :
                        pct >= 70 ? "C" :
                                pct >= 60 ? "D" : "F";
    }
}
