package com.task.mathsequences;

import com.task.mathsequences.controller.FactorialsController;
import com.task.mathsequences.dto.response.FactorialResponseDto;
import com.task.mathsequences.service.FactorialService;
import com.task.mathsequences.utils.JwtTokenUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FactorialsController.class)
@AutoConfigureMockMvc(addFilters = false)
class FactorialControllerValidNumberTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FactorialService factorialService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public FactorialService factorialService() {
            return mock(FactorialService.class);
        }

        @Bean
        public JwtTokenUtils jwtTokenUtils() {
            return mock(JwtTokenUtils.class);
        }
    }

    private static final String ENDPOINT = "/api/v1/factorial/generate";
    private static final String PARAM_NUMBER = "number";

    private ResultActions performRequestWithNumber(String number) throws Exception {
        return mockMvc.perform(get(ENDPOINT)
                .param(PARAM_NUMBER, number)
                .principal(() -> "testuser"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "1000", "-1", "93", "FD", "fdsfsdafsdf", "", "5.1", "5,1"})
    void whenNumberIsOutOfRange(String number) throws Exception {
        performRequestWithNumber(number)
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "10", "11"})
    void whenNumberIsValid(String number) throws Exception {
        performRequestWithNumber(number)
                .andExpect(status().isOk());
    }

    @Test
    void whenNumberIsValidThenReturnsCorrect() throws Exception {
        String username = "testuser";
        int number = 10;
        List<Long> fibonacciSequence = List.of(1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L);

        LocalDateTime fixedTime = LocalDateTime.of(2025, 7, 12, 10, 0);

        FactorialResponseDto responseDto = new FactorialResponseDto(number, fixedTime, fibonacciSequence);

        when(factorialService.generateAndSave(username, number))
                .thenReturn(responseDto);

        performRequestWithNumber(String.valueOf(number))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            {
                                "inputNumber": 10,
                                "requestTime": "2025-07-12T10:00:00",
                                "factorialNumbers": [1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800]
                            }
                        """));

        verify(factorialService, times(1)).generateAndSave(username, number);
    }

}

