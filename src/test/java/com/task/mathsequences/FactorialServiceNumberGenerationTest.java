package com.task.mathsequences;

import com.task.mathsequences.repository.FactorialRepository;
import com.task.mathsequences.repository.UserRepository;
import com.task.mathsequences.service.impl.FactorialServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FactorialServiceNumberGenerationTest {

    @Mock
    private FactorialRepository factorialRepository;

    @Mock
    private UserRepository userRepository;

    private FactorialServiceImpl factorialService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        factorialService = new FactorialServiceImpl(factorialRepository, userRepository);
    }

    @Test
    void testGenerateFactorialNumberIs5() {
        int number = 5;

        List<Long> expected = List.of(1L, 2L, 6L, 24L, 120L);
        List<Long> actual = factorialService.generateFactorialNumbers(number);

        assertEquals(expected, actual);
    }

    @Test
    void testGenerateFactorialNumberIs0() {
        int number = 1;

        List<Long> expected = List.of(1L);
        List<Long> actual = factorialService.generateFactorialNumbers(number);

        assertEquals(expected, actual);
    }

    @Test
    void testGenerateFactorialNumberIs10() {
        int number = 10;

        List<Long> expected = List.of(1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L);
        List<Long> actual = factorialService.generateFactorialNumbers(number);

        assertEquals(expected, actual);
    }

}

