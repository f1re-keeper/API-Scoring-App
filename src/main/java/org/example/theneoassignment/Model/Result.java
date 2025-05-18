package org.example.theneoassignment.Model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Result {
    @NotNull(message = "score should not be null.")
    private double score;
    @NotNull(message = "ruleBreakdown should not be null.")
    private Map<String, Double> ruleBreakdown;
    @NotNull(message = "feedback should not be null.")
    private String feedback;
}
