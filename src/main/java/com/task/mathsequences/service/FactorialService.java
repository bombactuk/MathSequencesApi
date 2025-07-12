package com.task.mathsequences.service;

import com.task.mathsequences.dto.response.FactorialResponseDto;

import java.util.List;

public interface FactorialService {

    List<Long> generateFactorialNumbers(int number);

    FactorialResponseDto generateAndSave(String username, int number);

    List<FactorialResponseDto> getHistory(String username);

    void clearHistory(String username);

}
