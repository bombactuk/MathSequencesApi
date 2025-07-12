package com.task.mathsequences.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class FactorialResponseDto {

    private Integer inputNumber;
    private LocalDateTime requestTime;
    private List<Long> FactorialNumbers;

}