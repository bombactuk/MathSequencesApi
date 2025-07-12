package com.task.mathsequences.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FibonacciRequestDto {

    @Min(value = 1, message = "Number must be at least 1")
    @Max(value = 92, message = "Number must be at most 92")
    @NotNull(message = "The number cannot be empty")
    @Schema(description = "Number of Fibonacci sequence elements to generate", example = "10", minimum = "1", maximum = "92")
    private Integer number;

}
