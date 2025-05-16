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
    private int score;
    @NotNull(message = "ruleBreakdown should not be null.")
    private Map<String, Integer> ruleBreakdown;
    @NotNull(message = "feedback should not be null.")
    private String feedback;
}
