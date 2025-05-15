package org.example.theneoassignment.Rules;

import io.swagger.v3.oas.models.OpenAPI;
import org.example.theneoassignment.Model.Result;

public abstract class Rule {
    protected OpenAPI spec;

    public Rule(OpenAPI spec) {
        this.spec = spec;
    }

    public abstract Result eval();
}
