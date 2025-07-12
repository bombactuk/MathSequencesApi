package com.task.mathsequences.service;

import com.task.mathsequences.dto.response.FibonacciResponseDto;

import java.util.List;

public interface FibonacciService {

    List<Long> generateFibonacciNumbers(int number);

    FibonacciResponseDto generateAndSave(String username, int number);

    List<FibonacciResponseDto> getHistory(String username);

    void clearHistory(String username);

}
