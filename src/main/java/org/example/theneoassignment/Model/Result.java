package org.example.theneoassignment.Model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Result {
    @NotNull(message = "Name must not be null")
    private String name;

    //testing changes
    @NotNull(message = "Weight must not be null")
    private int weight;

    @NotNull(message = "Score must not be null")
    private int score;

    @NotNull(message = "Issues must not be null")
    private int issues;
}
