package com.task.mathsequences;

import com.task.mathsequences.repository.FibonacciRepository;
import com.task.mathsequences.repository.UserRepository;
import com.task.mathsequences.service.impl.FibonacciServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FibonacciServiceNumberGenerationTest {

    @Mock
    private FibonacciRepository fibonacciRepository;

    @Mock
    private UserRepository userRepository;

    private FibonacciServiceImpl fibonacciService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fibonacciService = new FibonacciServiceImpl(fibonacciRepository, userRepository);
    }

    @Test
    void testGenerateFibonacciNumberIs5() {
        int number = 5;

        List<Long> expected = List.of(0L, 1L, 1L, 2L, 3L);
        List<Long> actual = fibonacciService.generateFibonacciNumbers(number);

        assertEquals(expected, actual);
    }

    @Test
    void testGenerateFibonacciNumberIs0() {
        int number = 1;

        List<Long> expected = List.of(0L);
        List<Long> actual = fibonacciService.generateFibonacciNumbers(number);

        assertEquals(expected, actual);
    }

    @Test
    void testGenerateFibonacciNumberIs10() {
        int number = 10;

        List<Long> expected = List.of(0L, 1L, 1L, 2L, 3L, 5L, 8L, 13L, 21L, 34L);
        List<Long> actual = fibonacciService.generateFibonacciNumbers(number);

        assertEquals(expected, actual);
    }

}
