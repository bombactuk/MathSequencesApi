package com.task.mathsequences.controller;

import com.task.mathsequences.dto.request.FibonacciRequestDto;
import com.task.mathsequences.dto.response.FibonacciResponseDto;
import com.task.mathsequences.service.FibonacciService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fibonacci")
@RequiredArgsConstructor
@Tag(name = "Fibonacci", description = "Operations with Fibonacci sequences")
public class FibonacciController {
    private final FibonacciService fibonacciService;

    @Operation(
            summary = "Generate Fibonacci sequence",
            description = "Generates first N Fibonacci numbers for authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sequence generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameter"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/generate")
    public ResponseEntity<FibonacciResponseDto> generate(
            @Valid FibonacciRequestDto dto,
            Principal principal) {
        return ResponseEntity.ok(fibonacciService.generateAndSave(principal.getName(), dto.getNumber()));
    }

    @Operation(
            summary = "Get Fibonacci request history",
            description = "Returns history of all Fibonacci sequence requests for current user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/history")
    public ResponseEntity<List<FibonacciResponseDto>> history(Principal principal) {
        return ResponseEntity.ok(
                fibonacciService.getHistory(principal.getName())
        );
    }

    @Operation(
            summary = "Clear Fibonacci request history",
            description = "Marks all Fibonacci requests as disabled for current user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History cleared successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/history/clear")
    public ResponseEntity<Void> clearHistory(Principal principal) {
        fibonacciService.clearHistory(principal.getName());
        return ResponseEntity.ok().build();
    }

}
