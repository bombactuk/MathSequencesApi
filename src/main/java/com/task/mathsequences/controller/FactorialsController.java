package com.task.mathsequences.controller;

import com.task.mathsequences.dto.request.FactorialRequestDto;
import com.task.mathsequences.dto.response.FactorialResponseDto;
import com.task.mathsequences.service.FactorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/factorial")
@RequiredArgsConstructor
@Tag(name = "Factorials", description = "Operations with Factorials sequences")
public class FactorialsController {
    private final FactorialService factorialService;

    @Operation(
            summary = "Generate Factorials sequence",
            description = "Generates first N Factorials numbers for authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sequence generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameter"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/generate")
    public ResponseEntity<FactorialResponseDto> generate(
            @Valid FactorialRequestDto dto,
            Principal principal) {
        return ResponseEntity.ok(factorialService.generateAndSave(principal.getName(), dto.getNumber()));
    }

    @Operation(
            summary = "Get Factorials request history",
            description = "Returns history of all Factorials sequence requests for current user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/history")
    public ResponseEntity<List<FactorialResponseDto>> history(Principal principal) {
        return ResponseEntity.ok(
                factorialService.getHistory(principal.getName())
        );
    }

    @Operation(
            summary = "Clear Factorials request history",
            description = "Marks all Factorials requests as disabled for current user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History cleared successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/history/clear")
    public ResponseEntity<Void> clearHistory(Principal principal) {
        factorialService.clearHistory(principal.getName());
        return ResponseEntity.ok().build();
    }

}